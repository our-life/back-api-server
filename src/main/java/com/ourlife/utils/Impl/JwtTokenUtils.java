package com.ourlife.utils.Impl;

import com.ourlife.config.JwtTokenProperties;
import com.ourlife.entity.User;
import com.ourlife.utils.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenUtils implements TokenProvider {

    private final JwtTokenProperties jwtTokenProperties;

    /**
     * methodName : createToken
     * author : 김정민
     * description : 회원 정보를 기반으로 jwt 토큰을 생성한다.
     *               토큰의 유효시간은 1시간이다.
     *
     * @param user 회원 정보
     * @return string 토큰
     */
    public String generateAccessToken(User user) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(user.getId()));
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtTokenProperties.getAccessTokenValidPeriod()))
                .signWith(SignatureAlgorithm.HS256, jwtTokenProperties.getSecretKey())
                .compact();
    }

    /**
     * methodName : getUserEmail
     * author : 김정민
     * description : token에서 회원정보(이메일 추출)
     *
     * @param token jwt token
     * @return string 회원 이메일
     */
    public Long parseUserIdFrom(String token) {
        return Long.parseLong(Jwts.parser().setSigningKey(jwtTokenProperties.getSecretKey()).parseClaimsJws(token).getBody().getSubject());
    }


    /**
     * methodName : resolveToken
     * author : 김정민
     * description : 요청 Header에서 token을 가져온다
     *               token은 X-AUTH-TOKEN 키 값에 보관된다.
     * @param request servlet request
     * @return string token
     */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization").split(" ")[1];
    }

    /**
     * methodName : validateToken
     * author : 김정민
     * description : 토큰의 유효시간이 지나면 false
     *
     * @param  token 토큰
     * @return boolean 토큰 유효성 여부
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(jwtTokenProperties.getSecretKey()).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}