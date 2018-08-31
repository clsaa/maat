package com.clsaa.maat.result.exception;

import org.springframework.http.HttpStatus;


/**
 * 记录不存在，此类异常将被转换为Http 404
 *
 * @author 任贵杰
 */
public class NotFoundException extends AbstractResultException {
    private static final long serialVersionUID = 1L;

    /**
     * @param code    业务错误码
     * @param message 业务提示信息
     */
    public NotFoundException(int code, String message) {
        super(code, message, HttpStatus.NOT_FOUND);
    }

    /**
     * @param message 业务提示信息
     */
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
