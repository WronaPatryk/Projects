import sys
sys.path.insert(1, '/home/patryk_mscwrona/repos') # inserting a library's directory

from pyspark.sql import SparkSession
from pyspark.context import SparkContext
from pyspark.sql.functions import *
from pyspark.sql.types import *
import numpy as np
import matplotlib.pyplot as plt

from river import optim
from river import linear_model
from river import forest
from river import tree
from river import ensemble
from river import preprocessing
from river import metrics
from river import dummy
from river import utils
from river import drift
import datetime as dt

import matplotlib.pyplot as plt
import matplotlib.pylab as pylab
params = {'legend.fontsize': 'medium',
          'figure.figsize': (18, 18),
         'axes.labelsize': 'x-large',
         'axes.titlesize':'x-large',
         'xtick.labelsize':'large',
         'ytick.labelsize':'large'}
pylab.rcParams.update(params)

from Fleuve.master import stream_miner_master
from Fleuve.master import evaluator_master
from Fleuve.master import predictor_master
from Fleuve.master import drift_detector_master

# from Fleuve.shared import stream_miner_handler
# from Fleuve.shared import evaluator_handler
# from Fleuve.shared import predictor_handler
# from Fleuve.shared import drift_detector_handler


from Fleuve.evaluation.initialize_scripts.initialize_agrawal import InitializeA1
from Fleuve.evaluation.initialize_scripts.initialize_agrawal import InitializeA2
from Fleuve.evaluation.initialize_scripts.initialize_agrawal import InitializeA3
from Fleuve.evaluation.initialize_scripts.initialize_agrawal import InitializeA4



# input data schema
input_schema = StructType([StructField("s",DoubleType(),True), \
    StructField("c",DoubleType(),True), \
    StructField("age",DoubleType(),True), \
    StructField("e", DoubleType(), True), \
    StructField("car", DoubleType(), True), \
    StructField("z", DoubleType(), True), \
    StructField("hv",DoubleType(),True), \
    StructField("hy",DoubleType(),True), \
    StructField("l", DoubleType(), True), \
    StructField("Class", DoubleType(), True)])



# path to stream miner worker files (transfered to workers within a spark context):
sc = SparkContext.getOrCreate()
sc.addPyFile("/home/patryk_mscwrona/repos/Fleuve/worker/evaluator.py")
sc.addPyFile("/home/patryk_mscwrona/repos/Fleuve/shared/evaluator_handler.py")
sc.addPyFile("/home/patryk_mscwrona/repos/Fleuve/worker/drift_detector.py")
sc.addPyFile("/home/patryk_mscwrona/repos/Fleuve/shared/drift_detector_handler.py")
sc.addPyFile("/home/patryk_mscwrona/repos/Fleuve/worker/stream_miner.py")
sc.addPyFile("/home/patryk_mscwrona/repos/Fleuve/shared/stream_miner_handler.py")
sc.addPyFile("/home/patryk_mscwrona/repos/Fleuve/worker/predictor.py")
sc.addPyFile("/home/patryk_mscwrona/repos/Fleuve/shared/predictor_handler.py")
sc.addPyFile("/home/patryk_mscwrona/repos/Fleuve/worker/caching.py")





#SET TASK
task = {"cluster-9e14-w-0": {"models": [tree.HoeffdingTreeClassifier(max_depth = 20)] , 
                             "min_update_freq_s" : 60, 
                             "min_training_instances": 197000}, 
        "cluster-9e14-w-1" : {"models": [tree.HoeffdingTreeClassifier(max_depth = 20)] , 
                             "min_update_freq_s" : 60, 
                             "min_training_instances": 197000}, 
        'workers': ["cluster-9e14-w-0", "cluster-9e14-w-1"],
       'eval_metrics': [metrics.Accuracy()]}

data_cols = [field.name for field in input_schema.fields][:-1]





for i in range(1,11):
    print("i = " + str(i))
    
    spark = SparkSession \
            .builder \
            .appName("ExecutorsTest") \
            .config("spark.executor.instances", "2") \
            .config("spark.executor.cores", "1") \
            .config("spark.executor.memory", "4g") \
            .getOrCreate()
    
    sdf = spark.read.format("csv").schema(schema = input_schema)\
        .options(header='true').load("hdfs://cluster-9e14-m/user/hdfs/stream/agrawal")
    sm = stream_miner_master.StreamMinerMaster(data_cols, task)
    sdf = sm.transform(sdf)
    
    sdf.select(*sm.result_cols).write.options(header='False', delimiter=',') \
     .csv("hdfs://cluster-9e14-m/user/hdfs/evaluation/ex2/case" + str(i))









