package com.clsaa.maat.task;

import com.clsaa.maat.constant.state.MessageState;
import com.clsaa.maat.model.dto.BusinessStatusDtoV1;
import com.clsaa.maat.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class MessageConfirmTask {

    public static final Logger LOGGER = LoggerFactory.getLogger(MessageConfirmTask.class);

    @Autowired
    private MessageService messageService;

    /**
     * 确定待确认状态的消ag息其主动方业务执sdf行结果, 上次执行完一分钟再执行一次
     */
    @Scheduled(cron = "0 0/1 * * * *")
    void confirmMessageStatus() {
        LOGGER.info("message confirm task begin");
        this.messageService.findMessageV1ByStatus(MessageState.待确认.getStateCode())
                .map(msg -> {
                    BusinessStatusDtoV1 businessStatusDtoV1 = this.messageService.confirmMessageStatus(msg.getQueryURL());
                    if (businessStatusDtoV1 == null) {
                        LOGGER.error("message confirm query return null, messageId: [{}]", msg.getMessageId());
                        return msg;
                    }
                    switch (businessStatusDtoV1.getBusinessStatus()) {
                        //如果业务已完成则消息转为发送中状态,修改状态时会将消息加入到重试队列中
                        case BusinessStatusDtoV1.STATUS_FINISHED:
                            this.messageService.updateStatusToSendingByMessageId(msg.getMessageId());
                            LOGGER.info("message confirm SENDING STATUS, messageId: [{}]", msg.getMessageId());
                            break;
                        //如果业务失败则消息转为已取消状态,修改状态时会将消息从重试队列移除
                        case BusinessStatusDtoV1.STATUS_FAILED:
                            this.messageService.updateStatusToCanceledByMessageId(msg.getMessageId());
                            LOGGER.info("message confirm CANCELED STATUS, messageId: [{}]", msg.getMessageId());
                            break;
                        case BusinessStatusDtoV1.STATUS_DOING:
                            LOGGER.info("message confirm DOING STATUS, messageId: [{}]", msg.getMessageId());
                            break;
                        default:
                            LOGGER.error("message confirm ERROR STATUS, messageId: [{}]", msg.getMessageId());
                            break;
                    }
                    return msg;
                })
                .collectList()
                .map(messages -> {
                    LOGGER.info("message confirm task finished, size : [{}]", messages.size());
                    return messages;
                });

    }
}
