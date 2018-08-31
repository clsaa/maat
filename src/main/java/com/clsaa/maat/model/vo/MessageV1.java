package com.clsaa.maat.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

/**
 * <p>
 * 消息持久层对象
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018/8/31
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageV1 {
    @Id
    private String id;

    private String topic;
}
