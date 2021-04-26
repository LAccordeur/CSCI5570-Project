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
- Python: Python3
- GeoMesa: 2.3.2
- Scala: 2.11

### How to use?
After installing all dependencies: 
#### Step 1: Download full dataset (optional)

