package com.clsaa.maat.constant;

/**
 * 支持的消息中间件,ROCKETMQ为RocketMQ
 *
 * @author 任贵杰
 * @summary 消息中间件枚举
 * @since 2018-09-01
 */
public enum SupportedMQEnum {
    /**
     * MessageQueue:RocketMQ
     */
    ROCKETMQ();

    SupportedMQEnum() {
    }

    /**
     * 通过消息中间件名称获取对应枚举,若不支持对应中间件则返回null
     *
     * @param name 消息中间件名称
     * @return {@link SupportedMQEnum}
     */
    public static SupportedMQEnum getByName(String name) {
        for (SupportedMQEnum supportedMQEnum : SupportedMQEnum.values()) {
            if (supportedMQEnum.name().equalsIgnoreCase(name)) {
                return supportedMQEnum;
            }
        }
        return null;
    }
}
