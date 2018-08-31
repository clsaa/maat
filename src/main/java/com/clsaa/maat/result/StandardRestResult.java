package com.clsaa.maat.result;

/**
 * 标准Rest输出,可指定业务码和业务提示信息
 *
 * @author 任贵杰
 */
public final class StandardRestResult implements RestResult {
    private final int code;
    private final String message;

    /**
     * 业务错误信息，默认错误码为：{@link RestResult.Codes#UNKNOWN}
     *
     * @param message 错误信息
     */
    public StandardRestResult(String message) {
        super();
        this.message = message;
        this.code = Codes.UNKNOWN;
    }

    /**
     * 可指定业务错误码和错误提示信息
     *
     * @param code    业务错误码
     * @param message 业务提示信息
     */
    public StandardRestResult(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "StandardRestResult [code=" + code + ", message=" + message + "]";
    }

}
