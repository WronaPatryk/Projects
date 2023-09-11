from river import datasets
import pandas as pd
import random
import string
import os
import time
import numpy as np



class Streamer(object):


    def __init__(self, river_generator):
        self.river_generator = river_generator

            
    def generateDataStream(self, n_in_one_file, max_files, gen_freq_s, CACHE_PATH, HDFS_PATH):

        dic = {}
        file_count = 0
        for xi, yi in self.river_generator.take(max_files*n_in_one_file):
            xi['Class'] = float(yi)
            dic[file_count] = xi
            file_count += 1
            # saving a file and persisting in HDFS
            if(file_count >= n_in_one_file):
                
                FILE_NAME = ''.join(random.choices(string.ascii_lowercase + string.digits, k=16))
                file_full_path = CACHE_PATH + "/" + FILE_NAME + ".csv"
                pd.DataFrame.from_dict(dic).transpose().to_csv(file_full_path, header=True, index = None)
                # HDFS
                os.system("hdfs dfs -moveFromLocal " + file_full_path + " " + HDFS_PATH + "/" + FILE_NAME + ".csv")

                file_count = 0
                dic = {}
                # WAIT gen_freq_s [s]
                time.sleep(gen_freq_s)
                
