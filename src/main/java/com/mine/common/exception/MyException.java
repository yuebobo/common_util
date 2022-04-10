package com.mine.common.exception;

import com.mine.common.JavaUtil;

/**
 * 自定义异常类
 */
public class MyException extends RuntimeException {

    private String errorMessage;

    private Exception e;

    public MyException() {
    }

    public MyException(String errorMessage, Exception e) {
        this.errorMessage = errorMessage;
        this.e = e;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Exception getE() {
        return e;
    }

    @Override
    public void printStackTrace() {
        if (e != null) {
            e.printStackTrace();
        }
    }

    @Override
    public String getMessage() {
        return e.getMessage();
    }

    public static MyException build(String errorMessage, Exception e) {
        if (e instanceof MyException) {
            MyException e1 = (MyException) e;
            if (!JavaUtil.isEmpty(errorMessage)) {
                e1.errorMessage = errorMessage + "=》" + e1.getMessage();
            }
            return e1;
        }
        return new MyException(errorMessage, e);
    }
}
