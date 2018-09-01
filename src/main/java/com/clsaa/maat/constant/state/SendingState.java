package com.clsaa.maat.constant.state;

/**
 * <p>
 * 主动方业务执行成功之后调用maat平台确认发送消息(将之前预发送的消息状态改为发送中)
 * 可转为 已死亡状态/已完成状态
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @summary 发送中状态
 * @since 2018-09-01
 */
public class SendingState extends AbstractState {
    @Override
    boolean doValidateState(MessageState stateTo) {
        return stateTo.equals(MessageState.死亡) || stateTo.equals(MessageState.已完成);
    }
}
