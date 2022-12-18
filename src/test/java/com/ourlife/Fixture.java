package com.ourlife;

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
}
