package com.mine.common;

import org.junit.Test;


/**
 * @author : zyb
 * 时间 : 2022/3/20 11:47
 */
public class JavaUtilTest {

    @Test
    public void isEmpty() {
    }

    @Test
    public void iteratorNext() {
    }

    @Test
    public void getFixedLengthNumber() {
        String s1 = MathUtil.getFixedLengthNumber(1, 3);
        assert "001".equals(s1);
        String s2 = MathUtil.getFixedLengthNumber(203, 2);
        assert "203".equals(s2);
        String s3 = MathUtil.getFixedLengthNumber(21.32, 4);
        assert "0021.32".equals(s3);
        String s4 = MathUtil.getFixedLengthNumber(43, 2);
        assert "43".equals(s4);
    }

    @Test
    public void scientificNotationConvert() {
        String flag = "E";
        assert "0.00".equals(MathUtil.scientificNotationConvert("0.00",flag));
        assert "0.01".equals(MathUtil.scientificNotationConvert("0.01E00",flag));
        assert "0.01".equals(MathUtil.scientificNotationConvert("0.01E+00",flag));
        assert "0.01".equals(MathUtil.scientificNotationConvert("0.01E-00",flag));
        assert "1.00".equals(MathUtil.scientificNotationConvert("0.01E+2",flag));
        assert "1.00".equals(MathUtil.scientificNotationConvert("0.01E+02",flag));
        assert "0.00001".equals(MathUtil.scientificNotationConvert("0.01E-3",flag));
        assert "0.00001".equals(MathUtil.scientificNotationConvert("0.01E-03",flag));
    }
}