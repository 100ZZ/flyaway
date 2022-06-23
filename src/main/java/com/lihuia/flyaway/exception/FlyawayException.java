package com.lihuia.flyaway.exception;

import com.lihuia.flyaway.response.ResponseCodeEnum;

/**
 * @author lihuia.com
 * @date 2022/6/20 8:36 PM
 */


public class FlyawayException extends RuntimeException {

    private static final long serialVersionUID = -1L;
    private ResponseCodeEnum responseCodeEnum = ResponseCodeEnum.FAIL;

    public FlyawayException() {
        super();
    }

    public FlyawayException(String message, Throwable cause, boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.responseCodeEnum.setMessage(message);
    }


    public FlyawayException(String message, Throwable cause) {
        super(message, cause);
        this.responseCodeEnum.setMessage(message);
    }

    public FlyawayException(String message) {
        super(message);
        this.responseCodeEnum.setMessage(message);
    }

    public FlyawayException(Throwable cause) {
        super(cause);
        this.responseCodeEnum.setMessage(cause.getMessage());

    }

    public FlyawayException(ResponseCodeEnum responseCodeEnum) {
        super(responseCodeEnum.getMessage());
        this.responseCodeEnum = responseCodeEnum;

    }

    public ResponseCodeEnum getResponseCodeEnum() {
        return this.responseCodeEnum;
    }

    @Override
    public String toString() {
        return "MysteriousException, errorCode: " + this.responseCodeEnum.getCode() + ", errorMsg: " + this.responseCodeEnum.getMessage();
    }
}
