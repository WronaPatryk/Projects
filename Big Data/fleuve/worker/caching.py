class Caching(object):
    
    def __init__(self, caching_max_size):
        self.caching_max_size = caching_max_size
        self.dic = dict()
        