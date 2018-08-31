package com.clsaa.maat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * maat是一个分布式事务中间件,实现了基于可靠消息的最终一致性事务控制.
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @summary Maat平台启动类
 * @since 2018-08-31
 */
@SpringBootApplication
public class MaatApplication {
    public static void main(String[] args) {
        SpringApplication.run(MaatApplication.class, args);
    }
}
