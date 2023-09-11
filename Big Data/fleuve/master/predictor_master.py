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
from river import utils

class PredictorMaster(object):
    
    def getOutputSchema(self, task, workers):
        
        
        output_schema_ = [
            StructField("training_time", FloatType(), False),
            StructField("n", IntegerType(), False),
            StructField("hostname", StringType(), False),
            StructField("hash", StringType(), False),
            StructField("model_name", StringType(), False),
            StructField("prediction", DoubleType(), False)
        ]

        n_models = np.max([len(models_lists) for models_lists in [model_list for model_list in [task[worker]['pretrained_models'] for worker in workers]]])

        if  n_models > 1:
            for i in range(2, n_models + 1):
                output_schema_.append(StructField("model_name_" + str(i), StringType(), False))
                output_schema_.append(StructField("prediction_" + str(i), DoubleType(), False))
          

        result_cols_ = ["results." + output_col.name for output_col in output_schema_]
        return StructType(output_schema_), result_cols_


    def __init__(self, data_cols : list, task : dict, label_col="Class",workers=["cluster-9e14-w-0", "cluster-9e14-w-1"]):
        # input_schema - StructType
        # data_cols - list of features for predicting with river's model == xi // must be in input_schema ################### TODO (check)
        # 'pretrained_models' MUST be a list for each worker

        output_schema, result_cols = self.getOutputSchema(task, workers)
        


        import predictor_handler
        f_ = udf(predictor_handler.PredictorHandler(task), output_schema)

        data_map = []
        for dc in data_cols:
            data_map.append(lit(dc))
            data_map.append(dc)

        
        # setting the values
        self.task = task   
        self.f_ = f_
        self.data_map = data_map
        self.label_col = label_col

        # plus, initial ones
        self.result_cols = result_cols
        self.output_schema = output_schema
        

        
    def transform_and_collect(self, sdf, OUTPUT_HDFS_PATH: str, OUTPUT_HDFS_CHECKPOINT_PATH: str):
        # transform step
        sdf = sdf.withColumn("data", create_map(*self.data_map))
        sdf = sdf.select(self.f_("data", "Class").alias("results")) # no support for multiple caching keys

        # collect step - saving as a stream
        query = sdf.select(*self.result_cols) \
            .writeStream \
            .format("csv")\
            .trigger(processingTime = "5 seconds")\
            .option("path", OUTPUT_HDFS_PATH)\
            .option("checkpointLocation", OUTPUT_HDFS_CHECKPOINT_PATH) \
            .start()

        query.awaitTermination()
        
        
    def transform(self, sdf):
        # transform step
        sdf = sdf.withColumn("data", create_map(*self.data_map))
        return sdf.withColumn('results', self.f_("data", "Class").alias("results")) # no support for multiple caching keys
    
    def collect(self, sdf, OUTPUT_HDFS_PATH: str, OUTPUT_HDFS_CHECKPOINT_PATH: str):
        # collect step - saving as a stream
        query = sdf.select(*self.result_cols) \
            .writeStream \
            .format("csv")\
            .trigger(processingTime = "5 seconds")\
            .option("path", OUTPUT_HDFS_PATH)\
            .option("checkpointLocation", OUTPUT_HDFS_CHECKPOINT_PATH) \
            .start()

        query.awaitTermination()
    
                
