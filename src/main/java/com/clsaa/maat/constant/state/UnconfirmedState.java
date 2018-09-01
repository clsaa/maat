package com.clsaa.maat.constant.state;

/**
 * <p>
 * 主动方业务执行之前先调用maat平台进行消息预发送, 预发送的消息存储后状态为待确认
 * 可转为 发送中状态/已取消状态
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @summary 待确认状态
 * @since 2018-09-01
 */
public class UnconfirmedState extends AbstractState {
    @Override
    boolean doValidateState(MessageState stateTo) {
        return stateTo.equals(MessageState.发送中) || stateTo.equals(MessageState.已取消);
    }
}
