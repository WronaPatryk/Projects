from drift_detector import DriftDetector

class DriftDetectorHandler(object):                          
    drift_detector = None
    
    def __init__(self, task_dict):
        self.task_dict = task_dict
    def __getstate__(self):
        return self.__dict__
    def __setstate__(self,d):
        self.__dict__ = d

    def __call__(self, xi, yi, caching_key=None):
        if not DriftDetectorHandler.drift_detector:
            DriftDetectorHandler.drift_detector = DriftDetector(self.task_dict)
        return DriftDetectorHandler.drift_detector.detect(xi, yi, caching_key)