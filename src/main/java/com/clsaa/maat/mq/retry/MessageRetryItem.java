package com.clsaa.maat.mq.retry;

import com.clsaa.maat.model.po.Message;
import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@AllArgsConstructor
public class MessageRetryItem implements Delayed {

    /**
     * 消息id
     */
    private String messageId;

    /**
     * 过期时间,预计发送消息时间戳(入队当前时间+配置的延迟时间)
     */
    private long expiredTime;

    public MessageRetryItem(String messageId) {
        this.messageId = messageId;
    }

    /**
     * 判断是否可以出队的条件,用过期时间(存活时间+创建时间)减当前时间戳
     *
     * @param unit 轮询时间单位
     */
    @Override
    public long getDelay(@NotNull TimeUnit unit) {
        return this.expiredTime - System.currentTimeMillis();
    }

    /**
     * 用于延时队列内部比较排序,当前延时时间-比较对象的延时时间,排序结果为升序
     *
     * @param retryItem 队列中的比较对象
     */
    @Override
    public int compareTo(@NotNull Delayed retryItem) {
        return Long.compare(this.expiredTime, ((MessageRetryItem) retryItem).expiredTime);
    }

    /**
     * 覆盖equals方法以便使用延迟队列的remove方法
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageRetryItem that = (MessageRetryItem) o;
        return that.messageId.equals(this.messageId);
    }

}
