package com.clsaa.maat.result;


/**
 * 默认实现
 */
public class DefaultRestResultProvider implements RestResultProvider {

    @Override
    public boolean support(Object source) {
        return source instanceof RestResult;
    }

    @Override
    public RestResult produce(Object source) {
        return (RestResult) source;
    }

}
