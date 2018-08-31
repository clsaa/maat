package com.clsaa.maat.result.exception;

import org.springframework.http.HttpStatus;

/**
 * 标准业务异常，对应Http 状态码： 417
 *
 * @author 任贵杰
 */
public class StandardBusinessException extends AbstractResultException {
    private static final long serialVersionUID = 1L;

    /**
     * @param code    业务错误码
     * @param message 业务提示信息
     */
    public StandardBusinessException(int code, String message) {
        super(code, message, HttpStatus.EXPECTATION_FAILED);
    }

    /**
     * @param message 业务提示信息
     */
    public StandardBusinessException(String message) {
        super(message, HttpStatus.EXPECTATION_FAILED);
    }

}
