package com.clsaa.maat.validator.common;

import com.clsaa.maat.config.BizCodes;
import com.clsaa.maat.result.BizAssert;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.constraints.NotNull;
import java.util.regex.Pattern;

/**
 * @author 任贵杰 812022339@qq.com
 * @summary URL校验器
 * @since 2018-09-02
 */
@Component
public class URLValidator implements Validator {

    public static final Pattern URL_PATTERN = Pattern.compile("^((ht|f)tps?)://([\\w\\-]+(\\.[\\w\\-]+)*/)*[\\w\\-]+(\\.[\\w\\-]+)*/?(\\?([\\w\\-.,@?^=%&:/~+#]*)+)?");

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public void validate(Object target, @NotNull Errors errors) {
        BizAssert.validParam(target != null, BizCodes.INVALID_PARAM.getCode(), "URL非法");
        String url = (String) target;
        BizAssert.validParam(URL_PATTERN.matcher(url).matches(), BizCodes.INVALID_PARAM.getCode(), "URL非法");
    }
}
