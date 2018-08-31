package com.clsaa.maat.controller;

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

    /**
     * 根据id查询消息
     *
     * @param id 消息id
     * @return 创建的APIid
     * @summary 创建API
     * @author 任贵杰 812022339@qq.com
     * @since 2018-05-24
     */
    @PostMapping(value = "/v1/message/{id}")
    public Mono<String> addApiV1(@PathVariable("id") String id) {
        return Mono.create(MonoSink::success);
    }

}
