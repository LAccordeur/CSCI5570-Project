/*
 * Copyright (c) 2013-2018 Commonwealth Computer Research, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0 which
 * accompanies this distribution and is available at
 * http://www.opensource.org/licenses/apache2.0.php.
 */

package com.rogerguo.spatial.geomesa.client;

import com.rogerguo.spatial.geomesa.data.CommonData;
import org.apache.commons.cli.ParseException;
import org.locationtech.geomesa.hbase.data.HBaseDataStoreFactory;

public class HBaseWriteClient extends GeoMesaClient {


    public HBaseWriteClient(String[] args, CommonData commonData, String logFilename) throws ParseException {
        super(args, new HBaseDataStoreFactory().getParametersInfo(), commonData, false, logFilename);
    }


    public static void main(String[] args) {
        try {

            String dataFilePath = "src/main/resources/dataset/trip_data_1_pickup.csv";
            String queryFilePath = "src/main/resources/query/test_query";
            String logFilePath = "src/main/resources/result/test/test_query_result.csv";
            HBaseWriteClient client = new HBaseWriteClient(args, new NYCTaxiFormattedDataTestGeoMesaZ2Synthetic(queryFilePath, dataFilePath), logFilePath);
            client.execute();



        } catch (ParseException e) {
            System.exit(1);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(2);
        }
        System.exit(0);
    }
}
