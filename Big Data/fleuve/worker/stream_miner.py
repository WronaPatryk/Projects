import time
from river import linear_model
from river import optim
from river import preprocessing # important
from river import dummy
# from river import datasets
from river import metrics
from river import utils
import socket
import string
import random
import pickle
import os
from caching import Caching
import numpy as np
import datetime as dt
import copy

class StreamMiner(object):
    
    def __init__(self, task_dic=None):
        
        task_dict = copy.deepcopy(task_dic)
        self.hostname = socket.gethostname()

        worker_dict = task_dict[self.hostname] # worker's hostname must be set within task_dict!
    
        if 'caching_max_size' in worker_dict.keys():
            self.caching = Caching(worker_dict['caching_max_size'])
        else:
            self.caching = None # no caching by default
        
        if 'scaler' in worker_dict.keys():
            self.scaler = worker_dict['scaler']
        else:
            self.scaler = None
#             self.scaler = preprocessing.StandardScaler() # default --- NO DEFAULT SCALER!
            
        if 'models' in worker_dict.keys():
            
            self.models = worker_dict['models']
            
            n_models = np.max([len(models_lists) for models_lists in [model_list for model_list in [task_dict[worker]['models'] for worker in task_dict['workers']]]])
            while(n_models > len(self.models)):
            
                self.models.append(dummy.NoChangeClassifier())
            
            self.model_names = [model.__class__.__name__ for model in self.models]     
            self.loaded_training_instances = [0 for model in self.models] # to be updated after loading

                          
                          
        if 'pretrained_models' in worker_dict.keys():
            self.models = []
            self.model_names = []
            self.loaded_training_instances = []
                   
            for pm in worker_dict['pretrained_models']:
                os.system('hdfs dfs -copyToLocal -f /user/hdfs/models/' + pm + " pretrained_model.pkl")
                # load
                with open('pretrained_model.pkl', 'rb') as f:
                    modelik = pickle.load(f)
                    self.models.append(copy.deepcopy(modelik))

                self.model_names.append(self.models[-1].__class__.__name__)
                self.loaded_training_instances.append(int(pm.split("_")[-1].split(".")[0]))
                
                
            n_models = np.max([len(models_lists) for models_lists in [model_list for model_list in [task_dict[worker]['pretrained_models'] for worker in task_dict['workers']]]])

            while(n_models > len(self.models)):
                self.models.append(dummy.NoChangeClassifier())
            
            self.model_names = [model.__class__.__name__ for model in self.models]
            
        
        # session hash
        self.hash = ''.join(random.choices(string.ascii_lowercase + string.digits, k=16))
        
        if 'min_update_freq_s' in worker_dict.keys():
            self.min_update_freq_s = worker_dict['min_update_freq_s']
        else:
            self.min_update_freq_s = 10000 # 10 000 s
            
        if 'min_training_instances' in worker_dict.keys():
            self.min_training_instances = worker_dict['min_training_instances']
        else:
            self.min_training_instances = 1000# 1000 instances
        
        # metrics
        self.metrics = [copy.deepcopy(task_dict['eval_metrics']) for model in self.models]
        # n_obs
        self.n = 0 # number of observation during the current training - Note that 'loaded_training_instances' keeps loaded model's n
        
        # starting time
        self.starting_time = time.time()
        self.last_update_time = self.starting_time
        
        self.PENDING_UPDATE_COUNT = 0
    
    
    def __getstate__(self):
        return self.__dict__
    def __setstate__(self,d):
        self.__dict__ = d
        
    # updates a metric, supports TimeRolling & Rolling
    def fleuve_update(self, metric, yi, yi_pred):
        if type(metric).__name__ == "TimeRolling":
            return metric.update(yi, yi_pred, t = dt.datetime.now())
        else:
            return metric.update(yi, yi_pred)
        

    def test_then_train(self, xi: dict, yi : float, caching_key=None): # currently no support for multiple caching keys
        # here input is important: xi: dict, yi: int (0 or 1)

        
        # First, possibly handle delayed labels/observations:
        # Assuming that exactly one of (xi,yi) has missing values
        delay_time = 0.0
        
        
        if self.caching is not None and caching_key is not None: # check if caching is enabled and there is a caching key column
            
            if not all([v is not None and v == v for v in xi.values()]): # YI CASE: xi has missing values!
                
                # try to get it from caching:
                if caching_key in self.caching.dic.keys(): # if it is in cache dict:
                    sub_dict = self.caching.dic[caching_key]
                    if 'xi' in sub_dict.keys():
                        xi = sub_dict['xi'] # HERE: we make a new prediction from xi.
                        delay_time = time.time() - sub_dict["ts"] # Note: if label comes earlier than data, then delay is negative
                        self.caching.dic.pop(caching_key) # delete the used observation from the cache
                    else: # the same observation with given caching_key arrived earlier!
                        overall_time = time.time() - self.starting_time
                          
                        # RESULT TIME:
                        res = [overall_time, self.n, self.hostname, self.hash]
                        for i in range(len(self.models)): # model_name_0, pred_0, [metrics_0] ... model_name_1, pred_1, [metrics_1]
                            res.append(self.model_names[i])
                            res.append(np.nan)
                            for eval_metric in self.metrics[i]:
                                res.append(np.nan)
                        res.append(caching_key)
                        res.append(np.nan)
                        return res
                        
                # there is no xi in caching dict
                else:
                    while len(self.caching.dic) > self.caching.caching_max_size:
                        (k := next(iter(self.caching.dic)), self.caching.dic.pop(k)) # deleting first elements until caching_max_size
                    self.caching.dic[caching_key] = {"yi" : yi, "ts" : time.time()} # adding new element to the dict
                    overall_time = time.time() - self.starting_time
                    
                    # RESULT TIME:
                    res = [overall_time, self.n, self.hostname, self.hash]
                    for i in range(len(self.models)): # model_name_0, pred_0, [metrics_0] ... model_name_1, pred_1, [metrics_1]
                        res.append(self.model_names[i])
                        res.append(np.nan)
                        for eval_metric in self.metrics[i]:
                            res.append(np.nan)
                    res.append(caching_key)
                    res.append(np.nan)
                    return res
                
            elif yi is None or yi != yi: # XI CASE: yi is missing!
                # it means xi is available!
                # Scale the features
                if self.scaler is not None:
                    xi = self.scaler.learn_one(xi).transform_one(xi)
                # make models predictions on the new "unobserved" instance xi
                yi_preds = [model.predict_one(xi) for model in self.models]
                
                # try to get it from caching:
                if caching_key in self.caching.dic.keys(): # if it is in cache dict:
                    sub_dict = self.caching.dic[caching_key]
                    if 'yi' in sub_dict.keys():
                        yi = sub_dict['yi']
                        delay_time = sub_dict["ts"] - time.time()
                        self.caching.dic.pop(caching_key) # delete the used observation from the cache
                    else: # the same observation with given caching_key arrived earlier!
                        overall_time = time.time() - self.starting_time
                        # here is to decide if the old prediction should be overwritten by a new one
                        
                        # RESULT TIME:
                        res = [overall_time, self.n, self.hostname, self.hash]
                        for i in range(len(self.models)): # model_name_0, pred_0, [metrics_0] ... model_name_1, pred_1, [metrics_1]
                            res.append(self.model_names[i])
                            res.append(yi_preds[i])
                            for eval_metric in self.metrics[i]:
                                res.append(np.nan)
                        res.append(caching_key)
                        res.append(np.nan)
#                         assert(len(res) == 14)
                        return res
                        
                # there is no yi in caching dict
                else:
                    while len(self.caching.dic) > self.caching.caching_max_size:
                        (k := next(iter(self.caching.dic)), self.caching.dic.pop(k)) # deleting first elements until caching_max_size
                    
                    # caching xi and each predictions (yi_preds)
                    self.caching.dic[caching_key] = {"xi" : xi.copy(), "ts" : time.time(), "prediction" : yi_preds.copy()} # adding new element to the dict, along with its prediction
                    overall_time = time.time() - self.starting_time
                    # RESULT TIME:
                    res = [overall_time, self.n, self.hostname, self.hash]
                    for i in range(len(self.models)): # model_name_0, pred_0, [metrics_0] ... model_name_1, pred_1, [metrics_1]
                        res.append(self.model_names[i])
                        res.append(yi_preds[i])
                        for eval_metric in self.metrics[i]:
                            res.append(np.nan)
                    res.append(caching_key)
                    res.append(np.nan)
#                     assert(len(res) == 14)
                    return res                          
            else:
                delay_time = 0.0
        # after any cache retrieval if there was any:
        
        # Scale the features -- no support for multiple scalers (per worker node)
        if self.scaler is not None:
            xi = self.scaler.learn_one(xi).transform_one(xi)
                          
        # make model predictions on the new "unobserved" instance xi
        yi_preds = [model.predict_one(xi) for model in self.models]
           
            
        # increment n - number of observations used for training during this training session
        self.n = self.n + 1
        # Train the models with the new sample
        for i in range(len(self.models)):
            self.models[i].learn_one(xi, yi)
        
        # update metrics
        self.metrics = [[ self.fleuve_update(self.metrics[o][t], yi, yi_preds[o]) for t in range(len(self.metrics[o]))] for o in range (len(self.metrics))]
    
        
        overall_time = time.time() - self.starting_time
        
        # checking if the model could be persisted, if so - persist the model
        if (time.time() - self.last_update_time >= self.min_update_freq_s) and self.n > self.min_training_instances:
            self.PENDING_UPDATE_COUNT += 1
            
            # save each model:
            for i in range(len(self.models)):
                model_dump_info = self.model_names[i] + "_" + self.hostname + "_" + str(self.n + self.loaded_training_instances[i]) 
                # saving locally
                with open(model_dump_info + '.pkl', 'wb') as f:
                    pickle.dump(self.models[i], f)
                # persisting in hdfs
#                 if self.PENDING_UPDATE_COUNT == 1:

                os.system('hdfs dfs -moveFromLocal ' + model_dump_info + '.pkl' + ' /user/hdfs/models/' + model_dump_info + '.pkl')
                os.system('rm -i *cluster-9e14*.pkl')
            
            self.last_update_time = time.time()
        
        # return id: 1) training time 2) hostname 3) hash 4) modelname + results: (prediction + metrics) + (if applicable) caching_key
        
        res = [overall_time, self.n, self.hostname, self.hash]
        for i in range(len(self.models)): # model_name_0, pred_0, [metrics_0] ... model_name_1, pred_1, [metrics_1]
            res.append(self.model_names[i])
            res.append(yi_preds[i])
            for eval_metric in self.metrics[i]:
                res.append(eval_metric.get())
#         if caching_key is not None:
        if self.caching is not None:
            res.append(caching_key)
            res.append(delay_time)

        return res

