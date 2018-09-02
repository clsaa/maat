package com.clsaa.maat.validator.dto;

import com.clsaa.maat.config.BizCodes;
import com.clsaa.maat.model.dto.MessageDtoV1;
import com.clsaa.maat.result.BizAssert;
import com.clsaa.maat.validator.common.URLValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.constraints.NotNull;

/**
 * @author 任贵杰 812022339@qq.com
 * @summary {@link MessageDtoV1} 参数校验器
 * @since 2018-09-02
 */
@Component
public class MessageDtoV1Validator implements Validator {

    @Autowired
    private URLValidator urlValidator;

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return MessageDtoV1.class.equals(clazz);
    }

    @Override
    public void validate(Object target, @NotNull Errors errors) {
        BizAssert.validParam(target!=null, BizCodes.INVALID_PARAM.getCode(),"传入参数为空");
        MessageDtoV1 messageDtoV1 = (MessageDtoV1) target;
        //message信息校验
        BizAssert.validParam(StringUtils.hasText(messageDtoV1.getVersion()),BizCodes.INVALID_PARAM.getCode(),
                "版本号不能为空" );
        BizAssert.validParam(StringUtils.hasText(messageDtoV1.getMessageId()),BizCodes.INVALID_PARAM.getCode(),
                "消息id不能为空,且能唯一标识业务" );
        BizAssert.validParam(StringUtils.hasText(messageDtoV1.getMessageBody()),BizCodes.INVALID_PARAM.getCode(),
                "消息内容不能为空" );
        BizAssert.validParam(StringUtils.hasText(messageDtoV1.getMessageQueue()),BizCodes.INVALID_PARAM.getCode(),
                "消息队列名不能为空" );
        BizAssert.validParam(StringUtils.hasText(messageDtoV1.getQueryURL()),BizCodes.INVALID_PARAM.getCode(),
                "queryURL不能为空, 必须提供URL供可靠消息服务查询业务状态" );
        this.urlValidator.validate(messageDtoV1.getQueryURL(), errors);
        BizAssert.validParam(StringUtils.hasText(messageDtoV1.getQueryURL()),BizCodes.INVALID_PARAM.getCode(),
                "queryURL不能为空, 必须提供URL供可靠消息服务查询业务状态" );
    }
}
