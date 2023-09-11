# fleuve

### "Integrating stream processing engines with stream mining libraries"

An integration of an existing stream mining library with a stream processing engine. Therefore, using the existing stream mining Python library *river* integrated with *Apache Spark* stream processing engine, a novel library for automating machine learning tasks for real-time data is proposed.

The repository has the following structure (alphabetically):

### config
- **gcp_dataproc_initial_setup.sh** - a script to be run on all nodes within the Hadoop cluster, stored in Google bucket, and used within **google_shell_dataproc_setup_command.txt**
- **google_shell_dataproc_setup_command.txt** - a text file containing a google cloud shell command for setting Google DataProc

### evaluation

- **/different_number_of_executors_scripts** - a directory containing scripts using which different number of spark executors were trained
- **/initialize_scripts** - a directory with several initialisation scripts for different evaluation scenarios. In order to omit, for instance, steps of setting the input data stream's scheme, or creating spark session and context -- multiple scripts have been provided so that in *.ipynb* files, the initialisation of the evaluation case is within one line.
- **.ipynb notebookes** -- Jupyter notebooks for different evaluation scenarios & their analysis

### master - .py files within the master module

- *stream_miner_master.py* - the StreamMinerMaster component
- *evaluator_master.py* - the EvaluatorMaster component
- *predictor_master.py* - the PredictorMaster component
- *drift_detector_master.py* - the DriftDetectorMaster component

### shared - .py files within the shared module (components' handlers)

### utils -  .py files within the utils module

- *streamer.py* - the Streamer component with its *generateDataStream()* function

### tests

Different scripts for initial testing of components. They could be run using */bin/spark-submit* Spark's command. They are comprised of all steps -- spark session creation, providing input schema, setting the spark context, defining fleuve's task, reading the input data stream, and fially transforming and collecting the results within a selected *HDFS* directory. They assume that the input and output directories are already created by the user.

### worker -  .py files within the worker module

- *stream_miner.py* - the StreamMiner component
- *evaluator.py* - the Evaluator component
- *predictor.py* - the Predictor component
- *drift_detector.py* - the DriftDetector component
- *caching.py* - the Caching component


