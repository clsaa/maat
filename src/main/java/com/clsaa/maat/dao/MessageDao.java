package com.clsaa.maat.dao;


import com.clsaa.maat.model.po.Message;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
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
     * 根据id查询消息
     *
     * @param id 消息id
     * @return {@link Mono<Message>}
     */
    Mono<Message> findMessageById(String id);

}
