package com.clsaa.maat.mq.retry;

import com.clsaa.maat.constant.state.MessageState;
import com.clsaa.maat.service.MessageService;
import com.clsaa.maat.utils.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * <p>
 * 消息重试队列, 会根据配置文件中retryWaitSeconds配置项,重试发送消息
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018-09-04
 */
@Component
public class MessageRetryQueue {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRetryQueue.class);

    private static final MessageRetryQueue MESSAGE_RETRY_QUEUE = new MessageRetryQueue();

    @Autowired
    private MessageService messageService;

    /**
     * 处理消息的线程池
     */
    private Executor executor = new ThreadPoolExecutor(2, 4, 120L, TimeUnit.SECONDS, new SynchronousQueue<>());

    /**
     * 创建一个最初为空的新延时队列
     */
    private DelayQueue<MessageRetryItem> messageRetryItemDelayQueue = new DelayQueue<>();


    /**
     * 防止被其他地方初始化
     */
    private MessageRetryQueue() {
    }

    /**
     * 获取实例(单例)
     *
     * @return {@link MessageRetryQueue}
     */
    public static MessageRetryQueue getInstance() {
        return MESSAGE_RETRY_QUEUE;
    }

    /**
     * 取元素守护线程
     */
    Thread daemonThread;

    /**
     * 初始化重试队列
     */
    @Component
    public class Builder implements ApplicationRunner {

        @Override
        public void run(ApplicationArguments args) {
            LOGGER.info("message retry queue init begin");
            long count = messageService.findMessageV1ByStatus(MessageState.发送中.getStateCode())
                    .collectList()
                    .block()
                    .parallelStream()
                    .peek(msg -> {
                        //将消息放到重试队列
                        messageService.putMessageToMessageRetryQueue(msg.getMessageId(), msg.getMessageTryTimes());
                    }).count();
            LOGGER.info("message retry queue init finished, size:[{}]", count);
            LOGGER.info("开始初始化消息重试队列守护线程...");
            daemonThread = new Thread(() -> MessageRetryQueue.getInstance().execute());
//            daemonThread = new Thread(MessageRetryQueue.this::execute); 无法正常执行, 不知道为什么, 待研究
            daemonThread.setDaemon(true);
            daemonThread.setName("MessageRetryQueueDaemonThread");
            daemonThread.start();
            LOGGER.info("消息重试队列守护线程初始化完成...");
        }
    }

    private void execute() {
        while (true) {
            MessageRetryItem messageRetryItem = null;
            try {
                //从延迟队列取值,取不到会一直等待
                messageRetryItem = this.messageRetryItemDelayQueue.take();
                final String messageId = messageRetryItem.getMessageId();
                LOGGER.info("从重试队列取出消息, messageId:[{}]", messageId);
                executor.execute(() -> Services.of(MessageService.class).sendMessage(messageId));
//                Services.of(MessageService.class).sendMessage(messageId);  // 方便在守护线程里打断点测试
            } catch (Exception e) {
                LOGGER.error("从重试队列取出消息失败, messageId:[{}], error: [{}]",
                        messageRetryItem.getMessageId(), e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 将消息加入到重试队列
     *
     * @param messageId   消息id(唯一标识业务)
     * @param expiredTime 过期时间(出队时间戳)
     */
    public void put(String messageId, long expiredTime) {
        LOGGER.info("加入到重试队列, messageId:[{}]", messageId);
        this.messageRetryItemDelayQueue.put(new MessageRetryItem(messageId, expiredTime));
    }

    /**
     * 将消息从队列中删除
     *
     * @param messageId 消息id(唯一标识业务)
     */
    public void remove(String messageId) {
        LOGGER.info("从重试队列删除, messageId:[{}]", messageId);
        this.messageRetryItemDelayQueue.remove(new MessageRetryItem(messageId));
    }
}
