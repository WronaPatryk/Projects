from stream_miner import StreamMiner

class StreamMinerHandler(object):                          
    stream_miner = None
    
    def __init__(self, task_dict):
        self.task_dict = task_dict
    def __getstate__(self):
        return self.__dict__
    def __setstate__(self,d):
        self.__dict__ = d

    def __call__(self, xi, yi, caching_key=None):
        if not StreamMinerHandler.stream_miner:
            StreamMinerHandler.stream_miner = StreamMiner(self.task_dict)
        return StreamMinerHandler.stream_miner.test_then_train(xi, yi, caching_key)