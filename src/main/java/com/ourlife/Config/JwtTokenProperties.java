package com.ourlife.Config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class JwtTokenProperties {

    @Value("${jwt.security.key}")
    private String secretKey;

    private long tokenValidPeriod;

    public JwtTokenProperties(String secretKey, long tokenValidPeriod) {
        this.secretKey = secretKey;
        this.tokenValidPeriod = tokenValidPeriod;
    }
}
