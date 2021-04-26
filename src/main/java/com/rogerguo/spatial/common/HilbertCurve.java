package com.rogerguo.spatial.common;

import com.google.uzaygezen.core.BitVector;
import com.google.uzaygezen.core.BitVectorFactories;
import com.google.uzaygezen.core.CompactHilbertCurve;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Date 2021/4/10 20:59
 * @Created by GUO Yang
 */
public class HilbertCurve implements SpaceFillingCurve {

    private CompactHilbertCurve compactHilbertCurve2D = new CompactHilbertCurve(new int[] {21, 21});

    @Override
    public String getCurveValueString(int x, int y) {
        List<Integer> bitsPerDimension = compactHilbertCurve2D.getSpec().getBitsPerDimension();
        BitVector[] points = new BitVector[bitsPerDimension.size()];
        for (int i = points.length; --i >= 0;) {
            points[i] = BitVectorFactories.OPTIMAL.apply(bitsPerDimension.get(i));
        }

        points[0].copyFrom(x);
        points[1].copyFrom(y);
        BitVector curveValue = BitVectorFactories.OPTIMAL.apply(compactHilbertCurve2D.getSpec().sumBitsPerDimension());
        compactHilbertCurve2D.index(points, 0, curveValue);

        return curveValue.toString();
    }


    public long getCurveValue(int x, int y) {
        List<Integer> bitsPerDimension = compactHilbertCurve2D.getSpec().getBitsPerDimension();
        BitVector[] points = new BitVector[bitsPerDimension.size()];
        for (int i = points.length; --i >= 0;) {
            points[i] = BitVectorFactories.OPTIMAL.apply(bitsPerDimension.get(i));
        }

        points[0].copyFrom(x);
        points[1].copyFrom(y);
        BitVector curveValue = BitVectorFactories.OPTIMAL.apply(compactHilbertCurve2D.getSpec().sumBitsPerDimension());
        compactHilbertCurve2D.index(points, 0, curveValue);

        return curveValue.toLong();
    }



    public Map<String, Integer> fromCurveValue(long curveValue) {
        return null;
    }
}

