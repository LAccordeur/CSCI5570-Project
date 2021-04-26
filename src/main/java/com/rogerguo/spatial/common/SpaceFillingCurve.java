package com.rogerguo.spatial.common;

/**
 * @Description
 * @Date 2021/4/9 21:28
 * @Created by GUO Yang
 */
import java.util.Map;

public interface SpaceFillingCurve {

    /**
     * long (byte[]) to string
     *
     * @param x
     * @param y
     * @return
     */
    String getCurveValueString(int x, int y);

    /**
     * only consider first 21 bits
     *
     * @param x >0 x31x30...x0
     * @param y >0 y31y30...y0
     * @return
     */
    long getCurveValue(int x, int y);

    Map<String, Integer> fromCurveValue(long curveValue);

}