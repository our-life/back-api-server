package com.ourlife.component;

import com.ourlife.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${jwt.security.key}")
    private String secretKey;

    public String generateToken(User user) {
        Date now = new Date();
        return Jwts.builder()
                .setHeader(createHeader())   // (1)
                .setClaims(createClaims(user))  // (2)
                .setIssuedAt(now)  // (3)
                .setExpiration(new Date(now.getTime()+ Duration.ofHours(3).toMillis())) // (4)
                .signWith(SignatureAlgorithm.HS256, secretKey)  // (5)
                .compact();
    }
    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ","JWT");
        header.put("alg","HS256"); // 해시 256 암호화
        return header;
    }

    private Map<String, Object> createClaims(User user) { // payload
        Map<String, Object> claims = new HashMap<>();
        claims.put("id",user.getId());
        claims.put("email",user.getEmail());
        return claims;
    }
}