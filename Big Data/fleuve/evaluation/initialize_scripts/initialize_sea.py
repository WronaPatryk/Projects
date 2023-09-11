from pyspark.sql import SparkSession
from pyspark.context import SparkContext
from pyspark.sql.functions import *
from pyspark.sql.types import *

class InitializeS():
    
    def __init__(self):

        self.spark = SparkSession \
            .builder \
            .appName("FriedmanTest") \
            .getOrCreate()

        # input data schema
        self.input_schema = StructType([StructField("V1",DoubleType(),True), \
            StructField("V2",DoubleType(),True), \
            StructField("V3",DoubleType(),True), \
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
    .options(header='true').load("hdfs://cluster-9e14-m/user/hdfs/stream/sea")

        
        
        