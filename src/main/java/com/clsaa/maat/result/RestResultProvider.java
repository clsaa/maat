package com.clsaa.maat.result;

/**
 * 业务码提供者，客户端可以自定义实现，客户端必须提供默认构造函数
 *
 * @author 任贵杰
 */
public interface RestResultProvider {
    /**
     * 是否支持此来源，如果source可以转换为RestResult，则返回true
     *
     * @param source never null
     * @return boolean
     */
    boolean support(Object source);

    /**
     * 生成一个RestMessage，包括code和message
     *
     * @param source never null
     * @return {@link RestResult}
     */
    RestResult produce(Object source);
}
