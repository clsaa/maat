package com.clsaa.maat.service;

import com.clsaa.maat.constant.state.MessageState;
import com.clsaa.maat.dao.MessageDao;
import com.clsaa.maat.model.po.Message;
import com.clsaa.maat.model.vo.MessageV1;
import com.clsaa.maat.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@Service
public class MessageService {

    @Autowired
    private MessageDao messageDao;

    /**
     * <p>
     * 添加消息,创建后消息的状态为 {@link MessageState#待确认}
     * </p>
     *
     * @param version      平台版本
     * @param messageId    消息id(用于唯一标志业务)
     * @param messageBody  消息内容
     * @param messageQueue 队列名称
     * @param queryURL     查询URL(用于状态确认)
     * @param remark       备注
     * @return {@link Mono<String>}
     * @summary 添加消息
     * @author 任贵杰 812022339@qq.com
     * @since 2018/9/2
     */
    public Mono<Message> addMessage(String version, String messageId, String messageBody, String messageQueue, String queryURL, String remark) {
        Message message = new Message();
        message.setVersion(version);
        message.setMessageId(messageId);
        message.setMessageBody(messageBody);
        message.setMessageTryTimes(Message.DEFAULT_TRY_TIMES);
        message.setMessageQueue(messageQueue);
        message.setMessageDead(Boolean.FALSE);
        message.setQueryURL(queryURL);
        message.setRemark(remark);
        message.setCtime(LocalDateTime.now());
        message.setMtime(LocalDateTime.now());
        message.setCuser(Message.DEFAULT_CUSER);
        message.setMuser(Message.DEFAULT_MUSER);
        message.setStatus(MessageState.待确认.getStateCode());
        return this.messageDao.save(message).onErrorMap(e -> {
            e.printStackTrace();
            return e;
        });
    }


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
