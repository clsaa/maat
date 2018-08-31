package com.clsaa.maat.utils;

import java.util.UUID;

/**
 * @author 任贵杰
 * @version v1
 * @summary UUID生成器
 * @since 2018/4/29
 */
public class UUIDUtil {
    private UUIDUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * 自动生成32位的UUid，对应数据库的主键id进行插入用。
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
