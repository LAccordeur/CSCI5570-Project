package com.rogerguo.spatial.geomesa.client;

import com.rogerguo.spatial.geomesa.data.CommonData;
import org.apache.commons.cli.ParseException;
import org.geotools.data.DataAccessFactory;
import org.geotools.data.Query;
import org.geotools.factory.Hints;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.locationtech.geomesa.hbase.data.HBaseDataStoreFactory;
import org.locationtech.geomesa.process.knn.KNNQuery;
import org.locationtech.geomesa.process.knn.NearestNeighbors;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * @Description
 * @Date 2021/4/16 22:08
 * @Created by GUO Yang
 */
public class KNNQueryClient extends GeoMesaClient {

    public KNNQueryClient(String[] args, CommonData data) throws ParseException {
        super(args, new HBaseDataStoreFactory().getParametersInfo(), data);
    }

    public static void main(String[] args) {
        try {
            KNNQueryClient client = new KNNQueryClient(args, new NYCTaxiFormattedDataTestGeoMesaZ2Synthetic(null, null));
            client.executeKNN();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void executeKNN() {
        int k = 50;
        double guessedDistance = 1000;
        double maxLimitDistance = 250000;

        SimpleFeatureType sft = this.getSimpleFeatureType(super.data);
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(sft);
        builder.set("seq_id", "null");
        builder.set("medallion", "null");
        builder.set("trip_time_in_secs", "null");
        builder.set("trip_distance", "null");
        builder.set("dtg", "null");

        double longitude = -73.952285;
        double latitude = 40.711963;
        builder.set("geom", "POINT (" + longitude + " " + latitude + ")");

        builder.featureUserData(Hints.USE_PROVIDED_FID, Boolean.TRUE);
        SimpleFeature searchPoint = builder.buildFeature("0");

        String timeFilter = String.format("dtg DURING %sT%s.000Z/%sT%s.000z", "2010-01-09", "09:09:16", "2010-01-10", "09:09:14");

        try {
            Query theQuery = new Query(super.data.getTypeName(), ECQL.toFilter(timeFilter));
            NearestNeighbors result = this.runKNN(super.data.getTypeName(), k, guessedDistance, maxLimitDistance, theQuery, searchPoint);
            System.out.println(result);
        } catch (CQLException e) {
            e.printStackTrace();
        }
    }

    public void testRangeQuery() {
        /*String during = String.format("dtg DURING %sT%s.000Z/%sT%s.000z", "2010-01-09", "09:09:16", "2010-01-09", "10:09:15");
        String bbox = String.format("bbox(geom,%s, %s, %s,%s)", lonMin, lonMax, latMin, latMax);
        System.out.println(during);
        System.out.println(bbox);
        Query query = new Query(super.data.getTypeName(), ECQL.toFilter(bbox + " AND " + during));
*/
    }
}
