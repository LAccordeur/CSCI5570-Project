package com.rogerguo.spatial.common;

import org.apache.hadoop.hbase.util.Bytes;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Date 2021/4/10 20:45
 * @Created by GUO Yang
 */
public class ZCurve implements SpaceFillingCurve {



    public String getCurveValueString(int x, int y) {
        byte[] ret = new byte[8];
        int xh = makeGap(x);
        int xl = makeGap(x << 16);
        int yh = makeGap(y) >>> 1;
        int yl = makeGap(y << 16) >>> 1;

        int zh = xh | yh;
        int zl = xl | yl;

        byte[] rh = CurveUtil.toBytes(zh);
        byte[] rl = CurveUtil.toBytes(zl);
        System.arraycopy(rh, 0, ret, 0, 4);
        System.arraycopy(rl, 0, ret, 4, 4);
        return CurveUtil.bytesToBit(ret);
    }


    /**
     *
     *
     * @param x >0 x31x30...x0
     * @param y >0 y31y30...y0
     * @return
     */
    public long getCurveValue(int x, int y) {
        byte[] ret = new byte[8];
        int xh = makeGap(x);
        int xl = makeGap(x << 16);
        int yh = makeGap(y) >>> 1;
        int yl = makeGap(y << 16) >>> 1;

        int zh = xh | yh;
        int zl = xl | yl;

        byte[] rh = CurveUtil.toBytes(zh);
        byte[] rl = CurveUtil.toBytes(zl);
        System.arraycopy(rh, 0, ret, 0, 4);
        System.arraycopy(rl, 0, ret, 4, 4);
        return CurveUtil.bytesToLong(ret);
    }


    public Map<String, Integer> fromCurveValue(long curveValue) {
        Map<String, Integer> resultMap = new HashMap<>();

        byte[] valueBytes = CurveUtil.toBytes(curveValue);

        int zh = Bytes.toInt(valueBytes, 0);
        int zl = Bytes.toInt(valueBytes, 4);

        int xh = elimGap(zh);
        int yh = elimGap(zh << 1);
        int xl = elimGap(zl) >>> 16;
        int yl = elimGap(zl << 1) >>> 16;

        int x = xh | xl;
        int y = yh | yl;
        resultMap.put("x", x);
        resultMap.put("y", y);

        return resultMap;
    }


    public static void main(String[] args) {
        ZCurve zCurve = new ZCurve();
        long result = zCurve.getCurveValue(11, 12);
        String resultString = zCurve.getCurveValueString(11, 12);
        System.out.println(result);
        System.out.println(resultString);
        System.out.println(zCurve.fromCurveValue(result));

    }

    private static final int[] MASKS = new int[]{0xFFFF0000, 0xFF00FF00,
            0xF0F0F0F0, 0xCCCCCCCC, 0xAAAAAAAA};

    private static int makeGap(int x) {
        int x0 = x & MASKS[0];
        int x1 = (x0 | (x0 >>> 8)) & MASKS[1];
        int x2 = (x1 | (x1 >>> 4)) & MASKS[2];
        int x3 = (x2 | (x2 >>> 2)) & MASKS[3];
        int x4 = (x3 | (x3 >>> 1)) & MASKS[4];
        return x4;
    }

    public static int elimGap(int x) {
        int x0 = x & MASKS[4];
        int x1 = (x0 | (x0 << 1)) & MASKS[3];
        int x2 = (x1 | (x1 << 2)) & MASKS[2];
        int x3 = (x2 | (x2 << 4)) & MASKS[1];
        int x4 = (x3 | (x3 << 8)) & MASKS[0];
        return x4;
    }
}