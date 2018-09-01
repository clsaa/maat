package com.clsaa.maat.constant.state;

import com.clsaa.maat.config.BizCodes;
import com.clsaa.maat.result.BizAssert;

/**
 * <p>
 * 状态模式
 * 消息状态的抽象父类，定义了状态的行为，用于校验修改消息状态是否合法
 * </p>
 *
 * @author 任贵杰
 * @version v1
 * @summary 抽象状态
 * @since 2018-09-01
 */
public abstract class AbstractState {

    /**
     * 校验的抽象实现，具体实现在子类中
     *
     * @param stateTo 目标状态{@link MessageState}
     * @return 验证通过
     */
    abstract boolean doValidateState(MessageState stateTo);

    /**
     * 校验消息的当前状态能否更新为目标状态
     *
     * @param stateTo 目标状态{@link MessageState}
     * @return 验证通过
     */
    boolean validateState(MessageState stateTo) {
        BizAssert.pass(doValidateState(stateTo), BizCodes.INVALID_MESSAGE_STATUS_CHANGING);
        return true;
    }
}