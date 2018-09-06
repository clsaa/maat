package com.clsaa.maat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;


/**
 * 业务方状态结果
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BusinessStatusDtoV1 implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务方业务流程已完成
     */
    public static final String STATUS_FINISHED = "FINISHED";
    /**
     * 业务方业务流程已失败
     */
    public static final String STATUS_FAILED = "FAILED";
    /**
     * 业务方流程进行中
     */
    public static final String STATUS_DOING = "DOING";

    private String messageId;
    private String businessStatus;
}
