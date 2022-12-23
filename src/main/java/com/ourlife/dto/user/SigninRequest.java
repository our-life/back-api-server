package com.ourlife.dto.user;

import com.ourlife.entity.User;
import com.ourlife.utils.PasswordEncoder;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SigninRequest {

    private String email;

    private String password;

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }
}
