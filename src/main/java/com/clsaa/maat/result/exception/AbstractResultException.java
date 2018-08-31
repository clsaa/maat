package com.clsaa.maat.result.exception;

import com.clsaa.maat.result.RestResult;
import com.clsaa.maat.result.StandardRestResult;
import org.springframework.http.HttpStatus;

/**
 * 所有业务接口异常的父类，异常对应不同的Http Status
 *
 * @author 任贵杰
 */
public class AbstractResultException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private RestResult result;
    private HttpStatus httpStatus;


    /**
     * 异常对应的http 状态吗
     *
     * @return {@link HttpStatus}
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    /**
     * 标准输出信息
     */
    public RestResult getResult() {
        return result;
    }


    /**
     * 指定异常消息提示，以及http 状态
     *
     * @param httpStatus HTTP状态码
     * @param message    业务提示
     */
    public AbstractResultException(String message, HttpStatus httpStatus) {
        this(RestResult.Codes.UNKNOWN, message, httpStatus);
    }

    /**
     * 设置异常对应的Http状态码，业务错误码，业务提示信息
     *
     * @param code       业务错误码
     * @param message    提示信息
     * @param httpStatus 标准http状态码
     */
    public AbstractResultException(int code, String message,
                                   HttpStatus httpStatus) {
        super(message, null, false, false);
        this.result = new StandardRestResult(code, message);
        this.httpStatus = httpStatus;
    }
}
