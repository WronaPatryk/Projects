from pyspark.sql import SparkSession
from pyspark.context import SparkContext
from pyspark.sql.functions import *
from pyspark.sql.types import *

class InitializeCCD():
    
    def __init__(self):

        self.spark = SparkSession \
            .builder \
            .appName("CreditCardDelayedTest") \
            .getOrCreate()

        # input data schema
        self.input_schema = StructType([StructField("Time",DoubleType(),True), \
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
            StructField("Class", DoubleType(), True), \
            StructField("caching_key", StringType(), True)])



        # path to stream miner worker files (transfered to workers within a spark context):
        sc = SparkContext.getOrCreate()
        sc.addPyFile("/home/patryk_patrykwrona/repos/Fleuve/worker/evaluator.py")
        sc.addPyFile("/home/patryk_patrykwrona/repos/Fleuve/shared/evaluator_handler.py")
        sc.addPyFile("/home/patryk_patrykwrona/repos/Fleuve/worker/drift_detector.py")
        sc.addPyFile("/home/patryk_patrykwrona/repos/Fleuve/shared/drift_detector_handler.py")
        sc.addPyFile("/home/patryk_patrykwrona/repos/Fleuve/worker/stream_miner.py")
        sc.addPyFile("/home/patryk_patrykwrona/repos/Fleuve/shared/stream_miner_handler.py")
        sc.addPyFile("/home/patryk_patrykwrona/repos/Fleuve/worker/predictor.py")
        sc.addPyFile("/home/patryk_patrykwrona/repos/Fleuve/shared/predictor_handler.py")
        sc.addPyFile("/home/patryk_patrykwrona/repos/Fleuve/worker/caching.py")
        self.sc = sc
        
        self.sdf = self.spark.readStream.format("csv").schema(schema = self.input_schema)\
    .options(header='true').load("hdfs://cluster-9e14-m/user/hdfs/credit_card_delayed")

        
        
        