package com.ourlife.dto.user;

import com.ourlife.entity.User;
import com.ourlife.utils.PasswordEncoder;
import lombok.*;

import java.time.LocalDate;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SignupRequest {
    private String password;

    private String nickname;

    private String email;

    private LocalDate birth;


    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .password(passwordEncoder.hash(this.password))
                .nickname(nickname)
                .email(email)
                .birth(birth)
                .build();
    }
}
