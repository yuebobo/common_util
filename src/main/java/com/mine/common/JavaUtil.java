package com.mine.common;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author : zyb
 * 时间 : 2022/3/13 22:00
 */
public class JavaUtil {


    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String) {
            return ((String) object).replace(" ","").length() <= 0;
        }
        if (object instanceof Collection) {
            return ((Collection<?>) object).size() <= 0;
        }
        if (object instanceof Map) {
            return ((Map<?, ?>) object).size() <= 0;
        }
        return false;
    }

    public static void iteratorNext(Iterator it, int count) {
        for (int i = 0; i < count; i++) {
            it.next();
        }
    }

}
