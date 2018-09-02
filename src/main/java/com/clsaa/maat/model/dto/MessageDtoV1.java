package com.clsaa.maat.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;

/**
 * <p>
 *  消息数据传输层对象
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018/9/2
 */
@Getter
@Setter
public class MessageDtoV1 implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 版本号
     */
    private String version;

    /**
     * 消息id,用于区分唯一业务
     */
    private String messageId;

    /**
     * 消息内容
     */
    private String messageBody;

    /**
     *  消息队列名
     */
    private String messageQueue;

    /**
     * queryURL, 由主动方应用提供, 用于消息状态确认调用
     */
    private String queryURL;

    /**
     * 备注
     */
    private String remark;
}
