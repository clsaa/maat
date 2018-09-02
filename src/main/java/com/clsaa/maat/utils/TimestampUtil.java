package com.clsaa.maat.utils;

import java.sql.Timestamp;

/**
 * @author 任贵杰
 * @version v1
 * @summary Time工具
 * @since 2018/4/29
 */
public class TimestampUtil {
    private TimestampUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * 自动生成32位的UUid，对应数据库的主键id进行插入用。
     */
    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }
}