package com.clsaa.maat.constant.state;

/**
 * <p>
 * 被动方业务完成后调用maat平台确认消息已被成功消费(消息状态转变为已完成)
 * 不可转变为任何其他状态
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @summary 已完成状态
 * @since 2018-09-01
 */
public class FinishedState extends AbstractState {
    @Override
    boolean doValidateState(MessageState stateTo) {
        return false;
    }
}
