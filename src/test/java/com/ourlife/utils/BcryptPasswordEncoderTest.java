package com.ourlife.utils;

import com.ourlife.Fixture;
import com.ourlife.utils.Impl.BcryptPasswordEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BcryptPasswordEncoderTest {

    BcryptPasswordEncoder passwordEncoder = new BcryptPasswordEncoder();

    @Test
    @DisplayName("비밀번호 암호화")
    void hashPwd() {
        String password = Fixture.user().getPassword();
        String hashedPassword = passwordEncoder.hash(password);
        assertThat(password).isNotEqualTo(hashedPassword);
        System.out.println(password);
        System.out.println(hashedPassword);
    }

    @Test
    @DisplayName("비밀번호 암호화 확인 맞는 비밀번호")
    void checkPwd_ture() {
        String password = Fixture.user().getPassword();
        String hashedPassword = passwordEncoder.hash(password);
        boolean result = passwordEncoder.match(password, hashedPassword);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("비밀번호 암호화 확인 틀린 비밀번호")
    void checkPwd_false() {
        String password = Fixture.user().getPassword();
        String wrongPassword = password + "dummy";
        String hashedPassword = passwordEncoder.hash(password);

        boolean result = passwordEncoder.match(wrongPassword, hashedPassword);
        assertThat(result).isFalse();
    }
}
