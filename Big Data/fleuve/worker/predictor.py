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

class Predictor(object):
    
    def __init__(self, task_dic=None):
        
        task_dict = copy.deepcopy(task_dic)
        self.hostname = socket.gethostname()

        worker_dict = task_dict[self.hostname] # worker's hostname must be set within task_dict!
        
        if 'scaler' in worker_dict.keys():
            self.scaler = worker_dict['scaler']
        else:
            self.scaler = None
#             self.scaler = preprocessing.StandardScaler() # default -- NO DEFAULT SCALER
                                
                          
        if 'pretrained_models' in worker_dict.keys():
            self.models = []
            self.model_names = []
                   
            for pm in worker_dict['pretrained_models']:
                os.system('hdfs dfs -copyToLocal -f /user/hdfs/models/' + pm + " pretrained_model.pkl")
                # load
                with open('pretrained_model.pkl', 'rb') as f:
                    self.models.append(pickle.load(f))
                self.model_names.append(self.models[-1].__class__.__name__)
                
            n_models = np.max([len(models_lists) for models_lists in [model_list for model_list in [task_dict[worker]['pretrained_models'] for worker in task_dict['workers']]]])
            while(n_models > len(self.models)):
                self.models.append(dummy.NoChangeClassifier())
            
            self.model_names = [model.__class__.__name__ for model in self.models]
       
        else:
            raise Exception("Pretrained models must be precised when using predictor!")
        
        
                    
        # session hash
        self.hash = ''.join(random.choices(string.ascii_lowercase + string.digits, k=16))
        
        # n_obs
        self.n = 0 # number of observation during the current evaluation - Note that 'loaded_training_instances' keeps loaded model's n
        
        # starting time
        self.starting_time = time.time()
    
    
    def __getstate__(self):
        return self.__dict__
    def __setstate__(self,d):
        self.__dict__ = d
        

    def predict(self, xi: dict, yi : float, caching_key=None): # currently no support for multiple caching keys
        # here input is important: xi: dict, yi: int (0 or 1)
  
        # Scale the features -- no support for multiple scalers (per worker node)
        if self.scaler is not None:
            xi = self.scaler.learn_one(xi).transform_one(xi)
                          
        # make model predictions on the new "unobserved" instance xi
        yi_preds = [model.predict_one(xi) for model in self.models]
           
            
        # increment n - number of observations used for evaluation during this training session
        self.n = self.n + 1

        
        overall_time = time.time() - self.starting_time

        # return id: 1) training time 2) hostname 3) hash 4) modelname + results: (prediction + metrics) + (if applicable) caching_key
        
        res = [overall_time, self.n, self.hostname, self.hash]
        for i in range(len(self.models)): # model_name_0, pred_0, model_name_1, pred_1, ...
            res.append(self.model_names[i])
            res.append(yi_preds[i])

        return res

