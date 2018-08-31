package com.clsaa.maat.result.exception;

import org.springframework.http.HttpStatus;

/**
 * 非法参数，此类异常将被转换为 Http Bad Request=400
 *
 * @author 任贵杰
 */
public class InvalidParameterException extends AbstractResultException {
    private static final long serialVersionUID = 1L;

    /**
     * @param code    业务错误码
     * @param message 业务提示信息
     */
    public InvalidParameterException(int code, String message) {
        super(code, message, HttpStatus.BAD_REQUEST);
    }


    /**
     * @param message 业务提示信息
     * @since 1.0.0
     */
    public InvalidParameterException(String message) {
        super(message, HttpStatus.BAD_REQUEST);

    }


}
