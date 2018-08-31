package com.clsaa.maat.result.exception;

import org.springframework.http.HttpStatus;

/**
 * 访问被拒绝，对应Http 403
 *
 * @author 任贵杰
 */
public class AccessDeniedException extends AbstractResultException {
    private static final long serialVersionUID = 1L;

    /**
     * @param code    业务错误码
     * @param message 提示消息
     */
    public AccessDeniedException(int code, String message) {
        super(code, message, HttpStatus.FORBIDDEN);
    }


    /**
     * @param message 业务提示信息
     */
    public AccessDeniedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }

}
