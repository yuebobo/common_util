package com.mine.common.exception;

import com.mine.common.JavaUtil;
import com.mine.common.annotation.Process;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : zyb
 * 时间 : 2022/3/19 9:14
 */
public class ExceptionUtil {

    private static final String black = "\n           ";

    /**
     * 根据给定的 Exception 获取相关的堆栈信息（代码执行）
     * @param e
     * @return
     */
    public static StringBuilder getStackTraceInfo(Exception e) {
        StringBuilder exceptionInfo = new StringBuilder();
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (StackTraceElement st : stackTrace) {
            if (st.getClassName().contains("com.mine")) {
                exceptionInfo.append("\r" + black);
                exceptionInfo.append(st);
            }
        }
        return exceptionInfo;
    }

    /**
     * 从 给定的 Exception 中 的堆栈链
     * 找到加了 {@link Process} 注解的方法
     * 把{@link Process} 中的 value 值 按顺序全部拼接成一个StringBuilder 并返回
     * @param e
     * @return
     * @throws ClassNotFoundException
     */
    public static StringBuilder getUserStackTraceInfo(Exception e) throws ClassNotFoundException {
        StringBuilder exceptionInfo = new StringBuilder();
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (int i = stackTrace.length - 1; i >= 0; i--) {
            StackTraceElement st = stackTrace[i];
            String className = st.getClassName();
            if (className.contains("com.mine")) {
                String methodName = st.getMethodName();
                List<Process> processAnnotations = getProcessAnnotation(className, methodName);
                if (!JavaUtil.isEmpty(processAnnotations)) {
                    exceptionInfo.append("\r" + black);
                    String value = processAnnotations.stream().map(Process::value).collect(Collectors.joining(" 或者 "));
                    exceptionInfo.append(value);
                }
            }
        }
        return exceptionInfo;
    }

    /**
     * 从给定的类型名和方法名中 找到 {@link Process}
     * @param className
     * @param methodName
     * @return
     * @throws ClassNotFoundException
     */
    public static List<Process> getProcessAnnotation(String className, String methodName) throws ClassNotFoundException {
        List<Process> processList = new ArrayList<>();
        Class<?> aClass = Class.forName(className);
        while (aClass != null) {
            Method[] ms = aClass.getDeclaredMethods();
            aClass = aClass.getSuperclass();
            List<Process> prs = Arrays.stream(ms).filter(p -> p.getName().equals(methodName) && p.getAnnotation(Process.class) != null)
                    .map(p -> p.getAnnotation(Process.class))
                    .collect(Collectors.toList());
            if (!JavaUtil.isEmpty(prs)) {
                processList.addAll(prs);
            }
        }
        return processList;
    }
}
