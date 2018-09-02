package com.clsaa.maat.result;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * Error默认返回内容配置类，覆盖:{@link DefaultErrorAttributes}
 * {@link EnableAutoConfiguration Auto-configuration} to render errors via a WebFlux
 * {@link org.springframework.web.server.WebExceptionHandler}.
 *
 * @author 任贵杰
 * @since 2.0.0
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnClass(WebFluxConfigurer.class)
@AutoConfigureBefore(WebFluxAutoConfiguration.class)
@EnableConfigurationProperties({ServerProperties.class, ResourceProperties.class})
public class ErrorWebFluxAutoConfiguration {

    private final ServerProperties serverProperties;

    public ErrorWebFluxAutoConfiguration(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @Bean
    @ConditionalOnMissingBean(value = ErrorAttributes.class, search = SearchStrategy.CURRENT)
    public JsonErrorAttributes errorAttributes() {
        return new JsonErrorAttributes(
                this.serverProperties.getError().isIncludeException());
    }

}