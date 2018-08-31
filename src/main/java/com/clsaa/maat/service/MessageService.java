package com.clsaa.maat.service;

import com.clsaa.maat.dao.MessageDao;
import com.clsaa.maat.model.vo.MessageV1;
import com.clsaa.maat.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MessageService {

    @Autowired
    private MessageDao messageDao;


    /**
     * 根据消息id查询消息
     *
     * @param messageId 消息id
     * @return {@link Mono<MessageV1>}
     */
    public Mono<MessageV1> findMessageV1ById(String messageId) {
        return this.messageDao
                .findMessageById(messageId)
                .map(m -> BeanUtils.convertType(m, MessageV1.class));
    }
}
