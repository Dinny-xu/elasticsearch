package com.study.elasticsearch.exception;

/**
 * @date 2020/7/20 16:28
 * @description: 基础异常。
 * <br/>1. 建议在业务中尽量使用JDK自带的异常。
 * <br/>2. 自定义异常继承BaseException。如：UserNotFoundException。
 */
public class BaseException extends RuntimeException {

    public BaseException() {
        super();
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }
}
