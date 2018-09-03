package com.clsaa.maat.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "maat")
@Validated
public class MaatProperties {
    @NotEmpty
    @Valid
    private List<Integer> retryWaitSeconds;
}
