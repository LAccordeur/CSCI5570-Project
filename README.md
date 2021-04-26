We investigate three different systems that use different ways to support spatial operations based on NoSQL key-value stores: GeoMesa, Pyro, MD-HBase
Since some systems do not open source their codes or their codes cannot run because of some version/dependency issues, we mainly focus on the evaluation of GeoMesa. 

The hadoop version we used is 2.7.7; HBase version is 1.4.11

### GeoMesa
- geomesa: a open-source spatial-temporal tools based on HBase
- official website: https://www.geomesa.org/
- we use its open-source code to test it
- it uses Z-curve as spatial index and use a top-to-bottom algorithm to generate scan range for spatial query. (https://www.geomesa.org/documentation/stable/tutorials/geohash-substrings.html)

### Dataset
Uri of full dataset: https://drive.google.com/file/d/1yQbDj6a-a8xOk9osF8mB3ZdVKZ0vSVTP/view?usp=sharing
  

### Directory
- src/main/java: evaluation codes
- src/main/python: code of generation of query workloads
- src/main/resources/dataset: only contains a subset of dataset, the full data set can be found in the above google driver uri.
- src/main/resources/query: the generated query workloads used in this project
- src/main/resources/result: the running result of query workloads

### Dependencies
- HBase: 1.4.11
- Java: JDK1.8
- Maven
- Python: Python3, Numpy
- GeoMesa: 2.2.1
- Scala: 2.11

### How to use?
After installing all dependencies: 
#### Step 1: Download full dataset (optional)

#### Step 2: Generate query workloads (optional)
- The workloads used in the evaluation have been generated and is located at the src/main/resources/query
- (Optional) generate it by yourself, use the command in the root of project directory
```
python3 src/main/python/SyntheticWorkload.py   // it needs 1~2 mintues because it needs to read pickup data file to generate data-related queries. 
```

#### Step 3: Run evaluation code
- Start HBase
```
cd /home/rogerguo/Tools/hbase-1.4.11/bin
./start-hbase.sh
```


- Insert data, it is handled by HBaseWriteClient.java (src/main/java/com/rogerguo/spatial/geomesa/client/HBaseWriteClient.java), you can specify input data file path in this code, and then run following commands in the root of project directory
```
mvn compile  // it needs several mintues when run at the first time (maven needs to download all denpendencies at the first time)
mvn exec:java -Dexec.mainClass=com.rogerguo.spatial.geomesa.client.HBaseWriteClient -Dexec.args="--hbase.zookeepers 127.0.0.1  --hbase.catalog geomesa_csci5570_test"   // it needs about 4~5 mintues to insert data
```

- Run range query workload, it is handled by HBaseClient.java, you can specify path of query workload and the path of output result, and then run following commands in the root of project directory, the response time of each query will be written in the output file.

```
mvn compile
mvn exec:java -Dexec.mainClass=com.rogerguo.spatial.geomesa.client.HBaseClient -Dexec.args="--hbase.zookeepers 127.0.0.1  --hbase.catalog geomesa_csci5570_test"

```

- Run k-NN query
```
mvn compile
mvn exec:java -Dexec.mainClass=com.rogerguo.spatial.geomesa.client.KNNQueryClient -Dexec.args="--hbase.zookeepers 127.0.0.1  --hbase.catalog geomesa_csci5570_test"
```

#### Virtual Box
We have installed all dependencies, generated query workload and inserted data in a virtual machine, so you can directly run queries using above commands in the root of project directory after starting HBase.