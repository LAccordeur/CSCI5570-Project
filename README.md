We choose three different systems that use different ways to support spatial operations based on NoSQL key-value stores. 
Since some systems do not open source their codes or their codes cannot run because of some version/dependency issues, 
we choose to implement their spatial index designs by ourselves according to their paper or documents as much as we can. 

The hadoop version we used is 2.7.7; HBase version is 1.4.11

- geomesa (a open-source spatial-temporal tools based on HBase)
  - official website: https://www.geomesa.org/
  - we use its open-source code to test it
  - it uses Z-curve as spatial index and use a top-to-bottom algorithm to generate scan range for spatial query. (https://www.geomesa.org/documentation/stable/tutorials/geohash-substrings.html)
- md-hbase (has open-source code, but the hadoop/hbase version used by it is too old)
  - MD-HBase: A Scalable Multi-dimensional Data Infrastructure for Location Aware Services
  - it has a demo version of code: https://github.com/shojinishimura/Tiny-MD-HBase
  - we modify it and make it can run on our selected versions of Hadoop and HBase
  - it combines Z-curve and builds extra index layer by using k-d tree and quad tree to index spatial data
- pyro (has open-source code, but they change the native code of hadoop and hbase, and its open-sourced code cannot run well)
  - Pyro: A Spatial-Temporal Big-Data Storage System
  - for spatial part, this paper mainly proposes geometry translator and an aggregation algorithm to optimize scan operations in spatial query and integrate it into hadoop/hbase framework.
  - this work is hard to implement since it needs to change the internal code of hadoop and hbase
  - thus we apply the optimization proposed in this paper and implement our version in the application level
  - besides, this paper does not give the details of their query algorithm, so we implement ours that can use their aggregation algorithm to optimize query.
  - in a word, the implementation of this work is different from original one, but implements an optimization for spatial query proposed in this paper.
  
