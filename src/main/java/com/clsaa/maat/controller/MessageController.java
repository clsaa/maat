package com.clsaa.maat.controller;

import com.clsaa.maat.model.dto.MessageDtoV1;
import com.clsaa.maat.model.po.Message;
import com.clsaa.maat.model.vo.MessageV1;
import com.clsaa.maat.service.MessageService;
import com.clsaa.maat.validator.dto.MessageDtoV1Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;
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
 * @since 2018/8/31
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
     * 添加消息,用于消息预存储,在主动方业务执行前盗用
     * </p>
     *
     * @param messageDtoV1 {@link MessageDtoV1}
     * @return {@link Mono<MessageV1>}
     * @summary 添加消息
     * @author 任贵杰 812022339@qq.com
     * @since 2018/9/1
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
     * 根据实体id查询消息
     * </p>
     *
     * @param id 实体id
     * @return {@link Mono<MessageV1>}
     * @summary 根据实体id查询消息
     * @author 任贵杰 812022339@qq.com
     * @since 2018/9/1
     */
    @GetMapping(value = "/v1/message/{id}")
    public Mono<MessageV1> findMessageByIdV1(@PathVariable("id") String id) {
        return this.messageService.findMessageV1ById(id);
    }

}
