package com.study.elasticsearch.exception;

/**
 * @date 2020/1/3 8:52
 * @description: 自定义业务异常
 */
public class BasicException extends BaseException {
    /**
     * 异常编码
     */
    private String code;

    public BasicException() {
    }

    public BasicException(String message) {
        super(message);
    }

    public BasicException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
