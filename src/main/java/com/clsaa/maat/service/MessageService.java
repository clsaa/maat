package com.clsaa.maat.service;

import com.clsaa.maat.config.BizCodes;
import com.clsaa.maat.config.MaatProperties;
import com.clsaa.maat.constant.state.MessageState;
import com.clsaa.maat.constant.state.StateContext;
import com.clsaa.maat.dao.MessageDao;
import com.clsaa.maat.model.dto.BusinessStatusDtoV1;
import com.clsaa.maat.model.po.Message;
import com.clsaa.maat.model.vo.MessageV1;
import com.clsaa.maat.mq.MessageQueueException;
import com.clsaa.maat.mq.MessageSender;
import com.clsaa.maat.mq.retry.MessageRetryQueue;
import com.clsaa.maat.result.BizAssert;
import com.clsaa.maat.result.exception.StandardBusinessException;
import com.clsaa.maat.utils.BeanUtils;
import com.clsaa.maat.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    private final MessageDao messageDao;

    @Autowired
    private MaatProperties maatProperties;

    @Autowired
    private MessageSender messageSender;

    public MessageService(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    /**
     * <p>
     * 将消息置入消息重试队列中,会根据当前重试次数确定消息出队时间
     * </p>
     *
     * @param messageId 消息id(唯一标识业务)
     * @summary 将消息置入消息重试队列中
     * @author 任贵杰 812022339@qq.com
     * @since 2018-09-05
     */
    public void putMessageToMessageRetryQueue(String messageId, int currentTryTimes) {
        //将消息放到重试队列
        MessageRetryQueue.getInstance().put(messageId,
                currentTryTimes == 0 ? System.currentTimeMillis() : System.currentTimeMillis() +
                        this.maatProperties.getRetryWaitSeconds().get(currentTryTimes - 1) * 1000);
    }

    /**
     * 根据消息状态查询全部消息
     *
     * @param status 消息状态
     * @return {@link Flux<MessageV1>}
     */
    public Flux<MessageV1> findMessageV1ByStatus(String status) {
        return this.messageDao.findAllByStatus(status)
                .map(msg -> BeanUtils.convertType(msg, MessageV1.class));
    }

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
        return this.messageDao
                .save(message)
                .onErrorMap(e -> new StandardBusinessException(BizCodes.ERROR_INSERT.getCode(), "添加失败"));
    }

    /**
     * @param messageId 消息id
     * @param statusTo  目标状态
     * @return {@link Mono<Void>}
     * @summary 根据实体id将消息修改到目标状态
     * @author 任贵杰 812022339@qq.com
     * @since 2018/9/3
     */
    private Mono<Message> updateStatusByMessageId(String messageId, String statusTo) {
        return this.messageDao.findMessageByMessageId(messageId)
                .flatMap(existMessage -> {
                    //幂等,当前状态和目标状态相同
                    if (existMessage.getStatus().equals(statusTo)) {
                        return Mono.just(existMessage);
                    }
                    //判断当前状态是否能改为目标状态
                    BizAssert.allowed(StateContext.validateState(existMessage.getStatus(), statusTo),
                            String.format("当前状态编码为%s,不能修改为%s", existMessage.getStatus(), statusTo));
                    existMessage.setStatus(statusTo);
                    existMessage.setMuser(Message.DEFAULT_MUSER);
                    existMessage.setMtime(LocalDateTime.now());
                    return this.messageDao.save(existMessage);
                });
    }

    /**
     * <p>
     * 根据消息id将消息状态更新为已取消
     * </p>
     *
     * @param messageId 消息id(唯一标识业务)
     * @return {@link Mono<MessageV1>}
     * @summary 更新消息状态为已取消
     * @author 任贵杰 812022339@qq.com
     * @since 2018-09-03
     */
    public Mono<MessageV1> updateStatusToCanceledByMessageId(String messageId) {
        return this.updateStatusByMessageId(messageId, MessageState.已取消.getStateCode())
                .map(m -> {
                    //将已取消的消息从重试队列移除
                    MessageRetryQueue.getInstance().remove(messageId);
                    return BeanUtils.convertType(m, MessageV1.class);
                });
    }

    /**
     * <p>
     * 根据消息id将消息状态更新为发送中,会将消息加入到重试队列中
     * </p>
     *
     * @param messageId 消息id(唯一标识业务)
     * @return {@link Mono<MessageV1>}
     * @summary 更新消息状态为发送中
     * @author 任贵杰 812022339@qq.com
     * @since 2018-09-03
     */
    public Mono<MessageV1> updateStatusToSendingByMessageId(String messageId) {
        return this.updateStatusByMessageId(messageId, MessageState.发送中.getStateCode())
                .map(m -> {
                    //将消息放到重试队列
                    this.putMessageToMessageRetryQueue(m.getMessageId(), m.getMessageTryTimes());
                    return BeanUtils.convertType(m, MessageV1.class);
                });
    }

    /**
     * <p>
     * 根据消息id将消息状态更新为已完成,会从重试队列移除消息
     * </p>
     *
     * @param messageId 消息id(唯一标识业务)
     * @return {@link Mono<MessageV1>}
     * @summary 更新消息状态为已完成
     * @author 任贵杰 812022339@qq.com
     * @since 2018-09-03
     */
    public Mono<MessageV1> updateStatusToFinishedByMessageId(String messageId) {
        return this.updateStatusByMessageId(messageId, MessageState.已完成.getStateCode())
                .map(m -> {
                    //将消息从重试队列移除
                    MessageRetryQueue.getInstance().remove(m.getMessageId());
                    return BeanUtils.convertType(m, MessageV1.class);
                });
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
    public Mono<MessageV1> findMessageV1ByMessageId(String messageId) {
        return this.messageDao.findMessageByMessageId(messageId)
                .map(m -> BeanUtils.convertType(m, MessageV1.class));
    }

    /**
     * 发送消息,并处理消息状态,
     * 如果消息不存在会静默处理,
     * 如果消息已经达到最大重试次数则会标记为死亡不再发送消息,
     * 如果消息未达到最大重试次数则会发送消息并将消息置入重试队列中
     *
     * @param messageId 消息id(唯一标识业务)
     */
    public void sendMessage(String messageId) {
        Message msg = this.messageDao.findMessageByMessageId(messageId).block();
        //如果消息已被删除则静默处理
        if (msg == null) {
            return;
        }
        if (msg.getStatus().equals(MessageState.发送中.getStateCode())) {
            //没死亡则判断重发次数,如果重发次数等于配置的最大重发次数则设置状态为已死亡,不再重发此消息
            //retryWaitSeconds: 10,20,30,40,50,60 即每隔10s,20s,30s,40s,50s重新发送一次,最后一次发送60s后标记为已死亡, 连上第一次共发送六次
            //如果前面已经发送了6次,最后一次发送60s后,取出消息MessageTryTimes=6 >= RetryWaitSeconds.size则标记为死亡状态,不再发送消息
            if (msg.getMessageTryTimes() >= this.maatProperties.getRetryWaitSeconds().size()) {
                LOGGER.info("消息已死亡, messageId:[{}]", messageId);
                msg.setMessageDead(true);
                msg.setStatus(MessageState.死亡.getStateCode());
                msg.setMtime(LocalDateTime.now());
                msg.setMuser(Message.DEFAULT_MUSER);
                this.messageDao.save(msg).block();
            } else {
                //发送消息
                try {
                    this.messageSender.send(msg.getMessageQueue(), msg.getMessageId(), msg.getMessageBody());
                } catch (MessageQueueException e) {
                    LOGGER.error("消息发送失败:", msg.toString());
                }
                //将消息发送次数加一并保存
                msg.setMessageTryTimes(msg.getMessageTryTimes() + 1);
                msg.setMtime(LocalDateTime.now());
                msg.setMuser(Message.DEFAULT_MUSER);
                this.messageDao.save(msg).block();
                //将消息放到重试队列
                this.putMessageToMessageRetryQueue(msg.getMessageId(), msg.getMessageTryTimes());
            }
        }
    }


    /**
     * 调用业务方提供的回查URL,确定业务当前状态
     *
     * @param queryURL 业务方提供的回查URL
     * @return {@link BusinessStatusDtoV1}
     */
    public BusinessStatusDtoV1 confirmMessageStatus(String queryURL) {
        return HttpUtil.get(queryURL, BusinessStatusDtoV1.class);
    }
}
