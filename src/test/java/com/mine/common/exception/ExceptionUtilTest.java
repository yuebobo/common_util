package com.mine.common.exception;

import com.mine.common.annotation.Process;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author : zyb
 * 时间 : 2022/3/19 11:26
 */
public class ExceptionUtilTest {

    private static final String xxx = "XXXXXX";
    private static final String yyy = "YYYYY";
    @Test
    public void getStackTraceInfo() {

    }

    @Process(xxx)
    private void getException(){
    }

    @Process(yyy)
    private void getException(Object object){
    }

    @Test
    public void getUserStackTraceInfo() {
    }

    @Test
    public void getProcessAnnotation() throws ClassNotFoundException {
        List<Process> processes = ExceptionUtil.getProcessAnnotation(ExceptionUtilTest.class.getName(), "getException");
        assert processes.size() == 2;
        Set<String> collect = processes.stream().map(Process::value).collect(Collectors.toSet());
        assert collect.contains(xxx);
        assert collect.contains(yyy);
    }
}