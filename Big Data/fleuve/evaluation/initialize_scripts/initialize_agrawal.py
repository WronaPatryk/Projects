from pyspark.sql import SparkSession
from pyspark.context import SparkContext
from pyspark.sql.functions import *
from pyspark.sql.types import *


class InitializeA():
    
    def __init__(self):

        self.spark = SparkSession \
            .builder \
            .appName("AgrawalTest") \
            .getOrCreate()

        # input data schema
        self.input_schema = StructType([StructField("s",DoubleType(),True), \
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
        self.sc = sc
        
        self.sdf = self.spark.readStream.format("csv").schema(schema = self.input_schema)\
    .options(header='true').load("hdfs://cluster-9e14-m/user/hdfs/stream/agrawal")

class InitializeA4():
    
    def __init__(self):

        self.spark = SparkSession \
            .builder \
            .appName("StructuredNetworkWordCount") \
            .config("spark.executor.instances", "4") \
            .config("spark.sql.shuffle.partitions", "2") \
            .appName("AgrawalTest") \
            .getOrCreate()

        # input data schema
        self.input_schema = StructType([StructField("s",DoubleType(),True), \
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
        self.sc = sc
        
        self.sdf = self.spark.read.format("csv").schema(schema = self.input_schema)\
    .options(header='true').load("hdfs://cluster-9e14-m/user/hdfs/stream/agrawal")
        
class InitializeA3():
    
    def __init__(self):

        self.spark = SparkSession \
            .builder \
            .appName("StructuredNetworkWordCount") \
            .config("spark.executor.instances", "3") \
            .config("spark.sql.shuffle.partitions", "2") \
            .appName("AgrawalTest") \
            .getOrCreate()

        # input data schema
        self.input_schema = StructType([StructField("s",DoubleType(),True), \
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
        self.sc = sc
        
        self.sdf = self.spark.read.format("csv").schema(schema = self.input_schema)\
    .options(header='true').load("hdfs://cluster-9e14-m/user/hdfs/stream/agrawal")

class InitializeA2():
    
    def __init__(self):

        self.spark = SparkSession \
            .builder \
            .appName("StructuredNetworkWordCount") \
            .config("spark.executor.instances", "2") \
            .config("spark.sql.shuffle.partitions", "2") \
            .appName("AgrawalTest") \
            .getOrCreate()

        # input data schema
        self.input_schema = StructType([StructField("s",DoubleType(),True), \
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
        self.sc = sc
        
        self.sdf = self.spark.read.format("csv").schema(schema = self.input_schema)\
    .options(header='true').load("hdfs://cluster-9e14-m/user/hdfs/stream/agrawal")
        
class InitializeA1():
    
    def __init__(self):

        self.spark = SparkSession \
            .builder \
            .appName("StructuredNetworkWordCount") \
            .config("spark.executor.instances", "1") \
            .config("spark.sql.shuffle.partitions", "2") \
            .appName("AgrawalTest") \
            .getOrCreate()

        # input data schema
        self.input_schema = StructType([StructField("s",DoubleType(),True), \
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
        self.sc = sc
        
        self.sdf = self.spark.read.format("csv").schema(schema = self.input_schema)\
    .options(header='true').load("hdfs://cluster-9e14-m/user/hdfs/stream/agrawal")


class InitializeA_CD():
    
    def __init__(self):

        self.spark = SparkSession \
            .builder \
            .appName("AgrawalTest") \
            .getOrCreate()

        # input data schema
        self.input_schema = StructType([StructField("s",DoubleType(),True), \
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
        sc.addPyFile("/home/patryk_mscwrona/repos/Fleuve/master/evaluator_handler.py")
        sc.addPyFile("/home/patryk_mscwrona/repos/Fleuve/worker/drift_detector.py")
        sc.addPyFile("/home/patryk_mscwrona/repos/Fleuve/master/drift_detector_handler.py")
        sc.addPyFile("/home/patryk_mscwrona/repos/Fleuve/worker/stream_miner.py")
        sc.addPyFile("/home/patryk_mscwrona/repos/Fleuve/master/stream_miner_handler.py")
        sc.addPyFile("/home/patryk_mscwrona/repos/Fleuve/worker/predictor.py")
        sc.addPyFile("/home/patryk_mscwrona/repos/Fleuve/master/predictor_handler.py")
        sc.addPyFile("/home/patryk_mscwrona/repos/Fleuve/worker/caching.py")
        self.sc = sc
        
        self.sdf = self.spark.readStream.format("csv").schema(schema = self.input_schema)\
    .options(header='true').load("hdfs://cluster-9e14-m/user/hdfs/stream/agrawal_cd")        
        
        