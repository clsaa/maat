package com.clsaa.maat.config;

import com.clsaa.maat.constant.SupportedMQEnum;
import com.clsaa.maat.mq.MessageSender;
import com.clsaa.maat.mq.rocketmq.RocketMQMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import javax.validation.constraints.NotBlank;

/**
 * 消息队列配置类, 根据配置maat.mq.name决定初始化哪个MQ的{@link MessageSender}实现
 */
@SpringBootConfiguration
public class MessageQueueConfig {
    public static final Logger LOGGER = LoggerFactory.getLogger(MessageQueueConfig.class);

    @NotBlank
    @Value("${maat.mq.name}")
    private String MQName;

    @Bean(name = "MessageSender")
    public MessageSender createMessageSender() {
        LOGGER.info("begin init message queue config : [{}]", MQName);

        if (SupportedMQEnum.getByName(MQName) == SupportedMQEnum.ROCKETMQ) {
            return new RocketMQMessageSender();
        }

        LOGGER.info("init message queue config fail : [{}]", MQName);
        //匹配不到抛出异常
        throw new UnsupportedOperationException(String.format("init message queue config fail, unsupported: [%s]", MQName));
    }
}
