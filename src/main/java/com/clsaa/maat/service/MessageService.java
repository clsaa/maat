package com.clsaa.maat.service;

import com.clsaa.maat.dao.MessageDao;
import com.clsaa.maat.model.po.Message;
import com.clsaa.maat.model.vo.MessageV1;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MessageService {

    @Autowired
    private MessageDao messageDao;


    public Mono<MessageV1> findMessageV1ById(String messageId) {
        return this.messageDao.findMessageById(messageId)
        .map(m->{
            MessageV1 messageV1 = new MessageV1();
            BeanUtils.copyProperties(m, messageV1);
            return messageV1;
        });
    }
}
