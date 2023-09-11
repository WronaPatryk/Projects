from predictor import Predictor

class PredictorHandler(object):                          
    predictor = None
    
    def __init__(self, task_dict):
        self.task_dict = task_dict
    def __getstate__(self):
        return self.__dict__
    def __setstate__(self,d):
        self.__dict__ = d

    def __call__(self, xi, yi, caching_key=None):
        if not PredictorHandler.predictor:
            PredictorHandler.predictor = Predictor(self.task_dict)
        return PredictorHandler.predictor.predict(xi, yi, caching_key)