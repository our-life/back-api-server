package com.ourlife;

import com.ourlife.dto.user.SignupRequest;
import com.ourlife.entity.User;

import java.time.LocalDate;

public class Fixture {

    public static User user() {
        return User.builder()
                .email("test@test.com")
                .introduce("자기소개 테스트")
                .nickname("test")
                .profileImgUrl("테스트url")
                .password("testPassword")
                .birth(LocalDate.now())
                .build();
    }

    public static User user(long userId) {
        return User.builder()
                .id(userId)
                .email("test@test.com")
                .introduce("자기소개 테스트")
                .nickname("test")
                .profileImgUrl("테스트url")
                .password("testPassword")
                .birth(LocalDate.now())
                .build();
    }

    public static SignupRequest signupRequest() {
        return SignupRequest.builder()
                .password("1234")
                .nickname("test")
                .email("test@test.com")
                .birth(LocalDate.now())
                .introduce("테스트자기소개")
                .build();
    }
}
