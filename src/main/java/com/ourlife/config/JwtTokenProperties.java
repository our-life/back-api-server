package com.ourlife.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConfigurationProperties("jwt")
public class JwtTokenProperties {

    private final String secretKey;

    private final Long accessTokenValidPeriod;

    @ConstructorBinding
    public JwtTokenProperties(String secretKey, Long accessTokenValidPeriod) {
        this.secretKey = secretKey;
        this.accessTokenValidPeriod = accessTokenValidPeriod;
    }
}
