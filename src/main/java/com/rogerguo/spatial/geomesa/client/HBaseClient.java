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

public class HBaseClient extends GeoMesaClient {


    public HBaseClient(String[] args, CommonData commonData, String logFilename) throws ParseException {
        super(args, new HBaseDataStoreFactory().getParametersInfo(), commonData, true, logFilename);
    }


    public static void main(String[] args) {
        try {

            String queryFilePath = "G:\\DataSet\\csci5570\\synthetic_01_1h_60_all_drz";
            String logFilePath = "G:\\DataSet\\csci5570\\log-new\\geomesa-z3-default-new\\synthetic_01_1h_60_all_drz.geomesa.default.z3.csv";
            HBaseClient client = new HBaseClient(args, new NYCTaxiFormattedDataTestGeoMesaZ2Synthetic(null, null), logFilePath);
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
