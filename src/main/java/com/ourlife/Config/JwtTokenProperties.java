package com.ourlife.Config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtTokenProperties {

    @Value("${jwt.security.key}")
    private String secretKey;

    private long tokenValidPeriod = 60 * 60 * 1000L;


/*    public JwtTokenProperties(String secretKey, long tokenValidPeriod) {
        this.secretKey = secretKey;
        this.tokenValidPeriod = tokenValidPeriod;
    }*/
}
