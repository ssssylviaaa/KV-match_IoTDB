package cn.edu.fudan.dsm.kvmatch.iotdb.utils;

import cn.edu.tsinghua.tsfile.common.exception.UnSupportedDataTypeException;
import cn.edu.tsinghua.tsfile.common.utils.Pair;
import cn.edu.tsinghua.tsfile.timeseries.read.support.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiaye Wu
 */
public class SeriesUtils {

    public static List<Double> amend(List<Pair<Long, Double>> keyPoints, Pair<Long, Long> interval) {
        List<Double> ret = new ArrayList<>();
        if (keyPoints.get(0).left >= interval.left) ret.add(keyPoints.get(0).right);
        for (int i = 1; i < keyPoints.size(); i++) {
            double k = 1.0 * (keyPoints.get(i).right - keyPoints.get(i - 1).right) / (keyPoints.get(i).left - keyPoints.get(i - 1).left);
            for (long j = keyPoints.get(i - 1).left + 1; j <= keyPoints.get(i).left; j++) {
                if (j >= interval.left && j <= interval.right) {
                    ret.add(keyPoints.get(i - 1).right + (j - keyPoints.get(i - 1).left) * k);
                }
            }
        }
        return ret;
    }

    public static List<Double> amend(List<Pair<Long, Double>> keyPoints) {
        return amend(keyPoints, new Pair<>(keyPoints.get(0).left, keyPoints.get(keyPoints.size() - 1).left));
    }

    public static double getValue(Field field) {
        switch (field.dataType) {
            case INT32:
                return field.getIntV();
            case INT64:
                return field.getLongV();
            case FLOAT:
                return field.getFloatV();
            case DOUBLE:
                return field.getDoubleV();
            default:
                throw new UnSupportedDataTypeException(String.valueOf(field.dataType));
        }
    }
}
