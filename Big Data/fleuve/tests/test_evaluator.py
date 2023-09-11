
from pyspark.sql import SparkSession
from pyspark.context import SparkContext

from pyspark.sql.functions import explode
from pyspark.sql.functions import split
from pyspark.sql.functions import udf
from pyspark.sql.functions import col
from pyspark.sql.functions import create_map
from pyspark.sql.functions import struct
from pyspark.sql.functions import lit
from pyspark.sql.types import *
import numpy as np

from river import optim
from river import linear_model
from river import forest
from river import preprocessing
from river import metrics
from river import dummy
from river import utils
import datetime as dt

from evaluator_master import EvaluatorMaster


spark = SparkSession \
    .builder \
    .appName("CreditCardTest") \
    .getOrCreate()

# input data schema
input_schema = StructType([StructField("Time",DoubleType(),True), \
    StructField("V1",DoubleType(),True), \
    StructField("V2",DoubleType(),True), \
    StructField("V3", DoubleType(), True), \
    StructField("V4", DoubleType(), True), \
    StructField("V5", DoubleType(), True), \
    StructField("V6",DoubleType(),True), \
    StructField("V7",DoubleType(),True), \
    StructField("V8", DoubleType(), True), \
    StructField("V9", DoubleType(), True), \
    StructField("V10", DoubleType(), True), \
    StructField("V11",DoubleType(),True), \
    StructField("V12",DoubleType(),True), \
    StructField("V13", DoubleType(), True), \
    StructField("V14", DoubleType(), True), \
    StructField("V15", DoubleType(), True), \
    StructField("V16",DoubleType(),True), \
    StructField("V17",DoubleType(),True), \
    StructField("V18", DoubleType(), True), \
    StructField("V19", DoubleType(), True), \
    StructField("V20", DoubleType(), True), \
    StructField("V21",DoubleType(),True), \
    StructField("V22",DoubleType(),True), \
    StructField("V23", DoubleType(), True), \
    StructField("V24", DoubleType(), True), \
    StructField("V25",DoubleType(),True), \
    StructField("V26",DoubleType(),True), \
    StructField("V27", DoubleType(), True), \
    StructField("V28", DoubleType(), True), \
    StructField("Amount", DoubleType(), True), \
    StructField("Class", DoubleType(), True)])



# path to stream miner worker files (transfered to workers within a spark context):
sc = SparkContext.getOrCreate()
sc.addPyFile("/home/patryk_patrykwrona/repos/Fleuve/master/evaluator.py")
sc.addPyFile("/home/patryk_patrykwrona/repos/Fleuve/master/evaluator_handler.py")
sc.addPyFile("/home/patryk_patrykwrona/repos/Fleuve/master/stream_miner.py")
sc.addPyFile("/home/patryk_patrykwrona/repos/Fleuve/master/stream_miner_handler.py")
sc.addPyFile("/home/patryk_patrykwrona/repos/Fleuve/master/caching.py")

#SET TASK
task = {"cluster-9e14-w-0": {"pretrained_models": ['ARFClassifier_cluster-9e14-w-0_223667.pkl','ARFClassifier_cluster-9e14-w-1_215236.pkl'], "scaler": preprocessing.StandardScaler()}, 
        "cluster-9e14-w-1" : {"pretrained_models": ['ARFClassifier_cluster-9e14-w-0_223667.pkl','ARFClassifier_cluster-9e14-w-1_215236.pkl'], "scaler": preprocessing.StandardScaler()},
       "eval_metrics": [metrics.BalancedAccuracy(), utils.TimeRolling(metrics.Accuracy(), period=dt.timedelta(seconds=10)), utils.Rolling(metrics.Accuracy(), 2000)], 'workers': ["cluster-9e14-w-0", "cluster-9e14-w-1"]}

# DO ANYTHING YOU WANT WITH SPARK SPE FUNCTIONALITIES
sdf = spark.readStream.format("csv").schema(schema = input_schema)\
    .options(header='true').load("hdfs://cluster-9e14-m/user/hdfs/credit_card")


# data_cols , LIST @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
data_cols = ['V' + str(i) for i in range(1,29)] # list as a user's input
data_cols.append('Amount') # as well, user's input
# label_col = "Class" (by default)

# STREAM MINER MSTER instance @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
sm = EvaluatorMaster(data_cols, task)
sm.transform_and_collect(sdf, "/user/hdfs/promotor_test", "/user/hdfs/promotor_test/chk")









