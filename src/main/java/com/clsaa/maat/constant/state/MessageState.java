package com.clsaa.maat.constant.state;

import com.clsaa.maat.result.exception.StandardBusinessException;
import lombok.Getter;

/**
 * <p>
 * 消息状态枚举
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018-09-01
 */
@Getter
public enum MessageState {
    /**
     * @see UnconfirmedState
     */
    待确认("UNCONFIRMED", UnconfirmedState.class),
    /**
     * @see SendingState
     */
    发送中("SENDING", SendingState.class),
    /**
     * @see CanceledState
     */
    已取消("CANCELED", CanceledState.class),
    /**
     * @see DeadState
     */
    死亡("DEAD", DeadState.class),
    /**
     * @see FinishedState
     */
    已完成("FINISHED", FinishedState.class);
    /**
     * 对应数据库的状态码
     */
    private String stateCode;
    /**
     * 对应状态模式的子类
     */
    private Class<? extends AbstractState> mappingStateClass;

    MessageState(String stateCode, Class<? extends AbstractState> mappingStateClass) {
        this.stateCode = stateCode;
        this.mappingStateClass = mappingStateClass;
    }

    public static MessageState getByCode(String stateCode) {
        for (MessageState messageState : values()) {
            if (messageState.stateCode.equals(stateCode)) {
                return messageState;
            }
        }
        throw new StandardBusinessException("invalid message status :" + stateCode);
    }
}