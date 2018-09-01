package com.clsaa.maat.constant.state;

/**
 * <p>
 * 主动方业务执行失败之后调用maat平台取消发送消息(将之前预发送的消息状态改为已取消)
 * 不可转为其他状态
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @summary 已取消状态
 * @since 2018-09-01
 */
public class CanceledState extends AbstractState{
    @Override
    boolean doValidateState(MessageState stateTo) {
        return false;
    }
}
