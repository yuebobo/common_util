package com.mine.common;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @author : zyb
 * 时间 : 2022/3/27 23:22
 */
public class MathUtil {

    public static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    /**
     * 把数字转成指定长度的数字，前边添0
     * 把 1 转成 001
     *
     * @param value
     * @param length
     * @return
     */
    public static String getFixedLengthNumber(Number value, int length) {
        String s = value.toString();
        int pointIndex = s.indexOf(".");
        String st;
        String ed = null;
        if (pointIndex > 0) {
            st = s.substring(0, pointIndex);
            ed = s.substring(pointIndex + 1);
            if (Integer.valueOf(ed) == 0) {
                ed = null;
            }
        } else {
            st = s;
        }
        int sub = length - st.length();
        if (sub > 0) {
            st = Integer.valueOf(Double.valueOf(Math.pow(10, sub)).intValue()).toString().replace("1", "") + st;
        }
        if (ed != null) {
            return st + "." + ed;
        }
        return st;
    }


    /**
     * 科学计数法转成普通的数字格式
     * 0.01E+2 转成 1.00
     *
     * @param x
     * @return
     */
    public static String scientificNotationConvert(String x, String flag) {
        if (!x.contains(flag)) {
            return x;
        }
        String[] split = x.split(flag);
        BigDecimal base = new BigDecimal(split[0]);
        if (base.compareTo(BigDecimal.ZERO) == 0) {
            return split[0];
        }
        Integer pow = new Integer(split[1]);
        BigDecimal multiply;
        if (pow >= 0) {
            multiply = BigDecimal.TEN.pow(pow);
        } else {
            multiply = BigDecimal.ONE;
            while (pow < 0) {
                multiply = multiply.divide(BigDecimal.TEN);
                pow++;
            }
        }

        return base.multiply(multiply).toString();
    }

    /**
     * 四舍五入 返回String类型
     *
     * @param value
     * @param precision 保留几位小数
     * @return
     */
    public static String getPrecisionString(String value, int precision) {
        if (value == null || value == "") return "0";
        Double dv = Double.valueOf(value);
        return getPrecisionString(dv, precision);
    }

    /**
     * 四舍五入 返回String类型
     *
     * @param value
     * @param precision 保留几位小数
     * @return
     */
    public static String getPrecisionString(double value, int precision) {
        double v = Math.abs(value);
        if (precision < 0) return String.format("%.0f", v);
        return (value >= 0 ? "" : "-") + String.format("%." + precision + "f", v);
    }

    /**
     * 四舍五入 获取百分数
     *
     * @param value
     * @param precision
     * @return
     */
    public static String getPercentage(String value, int precision) {
        String v = getPrecisionString(value, precision);
        String s = ONE_HUNDRED.multiply(new BigDecimal(v)).toString();
        return s.substring(0, s.length() - 2) + "%";
    }


    /**
     * 计算平均值
     * @param values
     * @return
     */
    public static double getAvg(String... values){
        return Arrays.stream(values).mapToDouble(Double::valueOf).sum() / values.length;
    }

    /**
     * 计算平均值
     * @param values
     * @return
     */
    public static double getAvg(double... values){
        return Arrays.stream(values).sum() / values.length;
    }
}
