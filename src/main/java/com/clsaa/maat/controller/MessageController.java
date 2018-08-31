package com.clsaa.maat.controller;

import com.clsaa.maat.model.vo.MessageV1;
import com.clsaa.maat.result.BizAssert;
import com.clsaa.maat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

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


    /**
     * <p>
     * 根据id查询消息
     * </p>
     *
     * @param messageId 消息id
     * @return {@link Mono<MessageV1>}
     * @summary 根据id查询消息
     * @author 任贵杰 812022339@qq.com
     * @since 2018/9/1
     */
    @GetMapping(value = "/v1/message/{messageId}")
    public Mono<MessageV1> findMessageV1ById(@PathVariable("messageId") String messageId) {
        return this.messageService.findMessageV1ById(messageId);
    }

}
