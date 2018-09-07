package com.clsaa.maat.mq.retry;

import com.clsaa.maat.mq.MessageSender;
import com.clsaa.maat.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.concurrent.*;

/**
 * <p>
 * 消息重试队列, 会根据配置文件中retryWaitSeconds配置项,重试发送消息
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018-09-04
 */
public class MessageRetryQueue {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRetryQueue.class);

    private static final MessageRetryQueue MESSAGE_RETRY_QUEUE = new MessageRetryQueue();
    @Autowired
    private MessageService messageService;

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
    public static MessageRetryQueue getInsance() {
        return MESSAGE_RETRY_QUEUE;
    }

    /**
     * 处理消息的线程池
     */
    private Executor executor = new ThreadPoolExecutor(1, 1, 120L, TimeUnit.SECONDS, new SynchronousQueue<>());

    /**
     * 创建一个最初为空的新延时队列
     */
    private DelayQueue<MessageRetryItem> messageRetryItemDelayQueue = new DelayQueue<>();

    /**
     * 监控消息队列的守护线程
     */
    private Thread daemonThread;

    /**
     * 初始化守护线程
     */
    @PostConstruct
    public void initDaemonThread() {
        LOGGER.info("开始初始化消息重试队列守护线程...");
        this.daemonThread = new Thread(this::execute);
        this.daemonThread.setDaemon(true);
        this.daemonThread.setName("Order Cancel Daemon Thread");
        this.daemonThread.start();
        LOGGER.info("消息重试队列守护线程初始化完成...");
    }

    private void execute() {
        while (true) {
            MessageRetryItem messageRetryItem = null;
            try {
                //从延迟队列取值,取不到会一直等待
                messageRetryItem = this.messageRetryItemDelayQueue.take();
                final String messageId = messageRetryItem.getMessageId();
                LOGGER.info("从重试队列取出消息, messageId:[{}]", messageId);
                this.executor.execute(() -> this.messageService.sendMessage(messageId));
            } catch (Exception e) {
                LOGGER.error("从重试队列取出消息失败, messageId:[{}], error: [{}]",
                        messageRetryItem.getMessageId(), e.getMessage());
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
