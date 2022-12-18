package com.ourlife.utils;

import com.ourlife.Config.JwtTokenProperties;
import com.ourlife.Fixture;
import com.ourlife.entity.User;
import com.ourlife.utils.Impl.JwtTokenUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JwtTokenUtilsTest {

    JwtTokenProperties jwtTokenProperties = new JwtTokenProperties("testKey", 1000 * 60L);

    JwtTokenUtils jwtTokenUtils = new JwtTokenUtils(jwtTokenProperties);

    @Test
    @DisplayName("토큰이 잘 생성되어야 한다.")
    void generateToken() {
        User user = Fixture.user(1L);
        String token = jwtTokenUtils.generateToken(user);
        Assertions.assertThat(token).isNotNull();
        System.out.println(token);
    }

    @Test
    @DisplayName("토큰에 유저 아이디가 들어있는지 확인")
    void parseUserIdFrom() {
        User user = Fixture.user(1L);
        String token = jwtTokenUtils.generateToken(user);
        Long userId = jwtTokenUtils.parseUserIdFrom(token);
        Assertions.assertThat(userId).isEqualTo(user.getId());
        System.out.println(userId);
    }
}
