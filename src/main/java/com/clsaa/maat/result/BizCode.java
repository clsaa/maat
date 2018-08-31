package com.clsaa.maat.result;

/**
 * 标准业务码
 *
 * @author 任贵杰
 */
public final class BizCode {
    private int code;
    private String message;

    public BizCode(int code, String message) {
        super();
        this.code = code;
        if (message == null) {
            message = "";
        }
        this.message = message;
    }

    /**
     * 业务相关的错误码
     */
    public int getCode() {
        return this.code;
    }

    /**
     * 错误信息
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * 将多个业务码组成字符串输出
     *
     * @param results 异常结果
     * @return 业务码字符串，比如：“21000,21020"
     */
    public static String toCodes(BizCode... results) {
        if (results == null || results.length == 0) {
            return "";
        }
        StringBuilder message = new StringBuilder();
        for (BizCode restResult : results) {
            message.append(",").append(restResult.getCode());
        }
        return message.deleteCharAt(0).toString();
    }
}
