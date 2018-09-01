package com.clsaa.maat.constant.state;

/**
 * <p>
 * 消息在发送中状态时,通过消息队列投递多次,消息状态仍未变成已完成则消息状态被改为死亡状态
 * 不可转变为任何其他状态
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @summary 死亡状态
 * @since 2018-09-01
 */
public class DeadState extends AbstractState {
    @Override
    boolean doValidateState(MessageState stateTo) {
        return false;
    }
}
