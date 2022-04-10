package com.mine.common.annotation;

import java.lang.annotation.*;

/**
 * @author : zyb
 * 时间 : 2022/3/19 9:35
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Process {

    String value();
}
