package com.clsaa.maat.controller;

import com.clsaa.maat.config.BizCodes;
import com.clsaa.maat.model.dto.MessageDtoV1;
import com.clsaa.maat.model.po.Message;
import com.clsaa.maat.model.vo.MessageV1;
import com.clsaa.maat.result.BizAssert;
import com.clsaa.maat.service.MessageService;
import com.clsaa.maat.validator.dto.MessageDtoV1Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * <p>
 * 消息接口实现类
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018-09-03
 */
@RestController
@CrossOrigin
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageDtoV1Validator messageDtoV1Validator;

    @InitBinder(value = "messageDtoV1")
    public void initMessageDtoV1ValidatorBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(this.messageDtoV1Validator);
    }

    /**
     * <p>
     * 添加消息,用于消息预存储,在主动方业务执行前调用
     * </p>
     *
     * @param messageDtoV1 {@link MessageDtoV1}
     * @return {@link Mono<MessageV1>}
     * @summary 添加消息
     * @author 任贵杰 812022339@qq.com
     * @since 2018-09-03
     */
    @PostMapping(value = "/v1/message")
    public Mono<Message> addMessageV1(@Validated @RequestBody MessageDtoV1 messageDtoV1) {
        return this.messageService.addMessage(
                messageDtoV1.getVersion(),
                messageDtoV1.getMessageId(),
                messageDtoV1.getMessageBody(),
                messageDtoV1.getMessageQueue(),
                messageDtoV1.getQueryURL(),
                messageDtoV1.getRemark());
    }

    /**
     * <p>
     * 根据消息id将消息状态更新为已取消,当主动方业务执行失败时调用此接口
     * </p>
     *
     * @param messageId 消息id(唯一标识业务)
     * @return {@link Mono<MessageV1>}
     * @summary 更新消息状态为已取消
     * @author 任贵杰 812022339@qq.com
     * @since 2018-09-03
     */
    @PutMapping(value = "/v1/message/status/toCanceled")
    public Mono<MessageV1> updateStatusToCanceledByMessageIdV1(@RequestParam("messageId") String messageId) {
        BizAssert.validParam(StringUtils.hasText(messageId),
                BizCodes.INVALID_PARAM.getCode(), "参数messageId非法");
        return this.messageService.updateStatusToCanceledByMessageId(messageId);
    }

    /**
     * <p>
     * 根据消息id将消息状态更新为发送中,当主动方业务执行成功时调用此接口
     * </p>
     *
     * @param messageId 消息id(唯一标识业务)
     * @return {@link Mono<MessageV1>}
     * @summary 更新消息状态为发送中
     * @author 任贵杰 812022339@qq.com
     * @since 2018-09-03
     */
    @PutMapping(value = "/v1/message/status/toSending")
    public Mono<MessageV1> updateStatusToSendingByMessageIdV1(@RequestParam("messageId") String messageId) {
        BizAssert.validParam(StringUtils.hasText(messageId),
                BizCodes.INVALID_PARAM.getCode(), "参数messageId非法");
        return this.messageService.updateStatusToSendingByMessageId(messageId);
    }

    /**
     * <p>
     * 根据消息id将消息状态更新为已完成,当被动方业务执行成功时调用此接口
     * </p>
     *
     * @param messageId 消息id(唯一标识业务)
     * @return {@link Mono<MessageV1>}
     * @summary 更新消息状态为已完成
     * @author 任贵杰 812022339@qq.com
     * @since 2018-09-03
     */
    @PutMapping(value = "/v1/message/status/toFinished")
    public Mono<MessageV1> updateStatusToFinishedByMessageIdV1(@RequestParam("messageId") String messageId) {
        BizAssert.validParam(StringUtils.hasText(messageId),
                BizCodes.INVALID_PARAM.getCode(), "参数messageId非法");
        return this.messageService.updateStatusToFinishedByMessageId(messageId);
    }

    /**
     * <p>
     * 根据消息id查询消息
     * </p>
     *
     * @param messageId 消息id(唯一标识业务)
     * @return {@link Mono<MessageV1>}
     * @summary 根据消息id查询消息
     * @author 任贵杰 812022339@qq.com
     * @since 2018-09-03
     */
    @GetMapping(value = "/v1/message")
    public Mono<MessageV1> findMessageByMessageIdV1(@RequestParam("messageId") String messageId) {
        BizAssert.validParam(StringUtils.hasText(messageId),
                BizCodes.INVALID_PARAM.getCode(), "参数messageId非法");
        return this.messageService.findMessageV1ByMessageId(messageId);
    }

}
