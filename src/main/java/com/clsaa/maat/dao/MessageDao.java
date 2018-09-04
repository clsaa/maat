package com.clsaa.maat.dao;


import com.clsaa.maat.model.po.Message;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>
 * 消息Dao
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018/8/31
 */
public interface MessageDao extends ReactiveCrudRepository<Message, String> {

    /**
     * 根据messageId删除消息
     *
     * @param messageId 消息id
     * @return {@link Mono<Void>}
     */
    Mono<Void> deleteByMessageId(String messageId);

    /**
     * 根据messageId查询消息
     *
     * @param messageId 消息id
     * @return {@link Mono<Message>}
     */
    Mono<Message> findMessageByMessageId(String messageId);

    /**
     * 根据消息状态查询全部消息
     * @param status 消息状态
     * @return {@link Flux<Message>}
     */
    Flux<Message> findAllByStatus(String status);
}
