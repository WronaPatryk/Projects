from evaluator import Evaluator

class EvaluatorHandler(object):                          
    evaluator = None
    
    def __init__(self, task_dict):
        self.task_dict = task_dict
    def __getstate__(self):
        return self.__dict__
    def __setstate__(self,d):
        self.__dict__ = d

    def __call__(self, xi, yi, caching_key=None):
        if not EvaluatorHandler.evaluator:
            EvaluatorHandler.evaluator = Evaluator(self.task_dict)
        return EvaluatorHandler.evaluator.evaluate(xi, yi, caching_key)