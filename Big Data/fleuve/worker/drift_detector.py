import time
from river import linear_model
from river import optim
from river import preprocessing # important
from river import dummy
from river import drift
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

class DriftDetector(object):
    
    def __init__(self, task_dic=None):
        
        
        task_dict = copy.deepcopy(task_dic)
        
        
        self.hostname = socket.gethostname()

        worker_dict = task_dict[self.hostname] # worker's hostname must be set within task_dict!


        if 'detectors' in worker_dict.keys():
            self.models = worker_dict['detectors']
            n_models = np.max([len(models_lists) for models_lists in [model_list for model_list in [task_dict[worker]['detectors'] for worker in task_dict['workers']]]])
            while(n_models > len(self.models)):
                self.models.append(drift.ADWIN())
            self.model_names = [model.__class__.__name__ for model in self.models]
        
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
        

    def detect(self, xi: dict, yi : float, caching_key=None): # currently no support for multiple caching keys
        # here input is important: xi: dict, yi: int (0 or 1)
        
        
        for key in xi.keys():
            first_obs = xi[key]
            break

        self.models = [model.update(first_obs) for model in self.models] # uses first of given data inputs
  
                          
        # make model predictions on the new "unobserved" instance xi
        yi_preds = [int(model.drift_detected) for model in self.models]
           
            
        # increment n - number of observations used for evaluation during this training session
        self.n = self.n + 1

        
        overall_time = time.time() - self.starting_time

        # return id: 1) training time 2) hostname 3) hash 4) modelname + results: (prediction + metrics) + (if applicable) caching_key
        
        res = [overall_time, self.n, self.hostname, self.hash]
        for i in range(len(self.models)): # model_name_0, pred_0, model_name_1, pred_1, ...
            res.append(self.model_names[i])
            res.append(yi_preds[i])

        return res

