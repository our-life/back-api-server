package com.ourlife;

import com.ourlife.dto.user.SigninRequest;
import com.ourlife.dto.user.SignupRequest;
import com.ourlife.dto.user.UpdateUserRequest;
import com.ourlife.entity.User;

import java.time.LocalDate;

public class Fixture {

    public static String token() {
        return "token";
    }

    public static UpdateUserRequest updateUserRequest() {
        return UpdateUserRequest.builder()
                .email("test@test.com")
                .introduce("자기소개 테스트")
                .nickname("test")
                .birth("19990517")
                .build();
    }

    public static User user() {
        return User.builder()
                .email("test@test.com")
                .introduce("자기소개 테스트")
                .nickname("test")
                .profileImgUrl("테스트url")
                .password("testPassword")
                .birth("19990517")
                .build();
    }
    public static User user1() {
        return User.builder()
                .email("testHwan@test.com")
                .introduce("자기소개 테스트asd")
                .nickname("testHwan")
                .profileImgUrl("테스트url")
                .password("testPassword")
                .birth("19990517")
                .build();
    }

    public static User user(long userId) {
        return User.builder()
                .id(userId)
                .email("test@test.com")
                .introduce("자기소개 테스트")
                .nickname("test123")
                .profileImgUrl("테스트url")
                .password("testPassword")
                .birth("19990517")
                .build();
    }

    public static SignupRequest signupRequest() {
        return SignupRequest.builder()
                .password("1234")
                .nickname("test")
                .email("test@test.com")
                .birth("19990517")
                .build();
    }

    public static SigninRequest signinRequest(){
        return SigninRequest.builder()
                .email("test@test.com")
                .password("testPassword")
                .build();
    }
}
