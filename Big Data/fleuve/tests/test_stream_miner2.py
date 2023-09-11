

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
from river import dummy
from river import tree

import f


spark = SparkSession \
    .builder \
    .appName("CreditCardLogisticRegressionTest2") \
    .getOrCreate()


# schema
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
sc.addPyFile("/home/patryk_patrykwrona/repos/Fleuve/master/stream_miner.py")
sc.addPyFile("/home/patryk_patrykwrona/repos/Fleuve/master/f.py")
sc.addPyFile("/home/patryk_patrykwrona/repos/Fleuve/master/caching.py")


output_schema = StructType([
    StructField("timestamp", FloatType(), False),
    StructField("hostname", StringType(), False),
    StructField("hash", StringType(), False),
    StructField("model_name", StringType(), False),
    StructField("prediction", IntegerType(), False),
    StructField("accuracy", FloatType(), False),
    StructField("cohen_kappa", FloatType(), False),
    StructField("balanced_accuracy", FloatType(), False)
])


task = {"cluster-9e14-w-0": {"model": tree.HoeffdingTreeClassifier(grace_period=50,delta=1e-5), "scaler": preprocessing.StandardScaler()}, "cluster-9e14-w-1" : {"model": dummy.NoChangeClassifier(), "scaler": preprocessing.StandardScaler()}}

f_ = udf(f.F(task), output_schema)

# Create DataFrame representing the stream of input lines from connection to cluster-9e14-m
sdf = spark.readStream.format("csv").schema(schema = input_schema)\
    .options(header='true').load("hdfs://cluster-9e14-m/user/hdfs/credit_card")

# variables = [f"V{str(i)}" for i in range(1, 29)] # useless?


sdf = sdf.withColumn("data", create_map( \
lit('V1'),'V1',\
lit('V2'),'V2',\
lit('V3'),'V3',\
lit('V4'),'V4',\
lit('V5'),'V5',\
lit('V6'),'V6',\
lit('V7'),'V7',\
lit('V8'),'V8',\
lit('V9'),'V9',\
lit('V10'),'V10',\
lit('V11'),'V11',\
lit('V12'),'V12',\
lit('V13'),'V13',\
lit('V14'),'V14',\
lit('V15'),'V15',\
lit('V16'),'V16',\
lit('V17'),'V17',\
lit('V18'),'V18',\
lit('V19'),'V19',\
lit('V20'),'V20',\
lit('V21'),'V21',\
lit('V22'),'V22',\
lit('V23'),'V23',\
lit('V24'),'V24',\
lit('V25'),'V25',\
lit('V26'),'V26',\
lit('V27'),'V27',\
lit('V28'),'V28',\
lit('Amount'), 'Amount'\
))
                     
res = sdf.select(f_("data", "Class").alias("results"))

# saving as a stream
query = res.select("results.timestamp", "results.hostname", "results.hash", "results.model_name", "results.prediction", "results.accuracy", "results.cohen_kappa", "results.balanced_accuracy") \
    .writeStream \
    .format("csv")\
    .trigger(processingTime = "5 seconds")\
    .option("path", "/user/hdfs/quick_test2")\
    .option("checkpointLocation", "/user/hdfs/quick_test2/chk") \
    .start()
               
query.awaitTermination()
                     
                     
                     
