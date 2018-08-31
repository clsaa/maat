package com.clsaa.maat.result;

import org.springframework.util.ClassUtils;

/**
 * 适配SPI VIEW
 *
 * @author 任贵杰 812022339@qq.com
 */
public class BizCodeAdaptorRestResultProvider implements RestResultProvider {
    /**
     * SPI VIEW是否存在
     */
    private final boolean bizcodeExist =
            ClassUtils.isPresent(BizCode.class.getName(),
                    this.getClass().getClassLoader());

    @Override
    public boolean support(Object source) {
        return bizcodeExist && source instanceof BizCode;
    }

    @Override
    public RestResult produce(Object source) {
        BizCode bizCode = (BizCode) source;
        return new StandardRestResult(bizCode.getCode(), bizCode.getMessage());
    }

}
