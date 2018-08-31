package com.clsaa.maat.result;

/**
 * 标准Rest输出,可指定业务码和业务提示信息
 *
 * @author 任贵杰
 */
public interface RestResult {
    /**
     * 业务码（比如业务错误码或者其他Code）
     *
     * @return code
     */
    int getCode();

    /**
     * 业务码的说明
     *
     * @return message
     */
    String getMessage();

    /**
     * 常见的内置业务码
     */
    interface Codes {
        /**
         * 未知原因
         */
        int UNKNOWN = -1;
    }
}
