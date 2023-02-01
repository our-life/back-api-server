package com.ourlife.dto.user;

import com.ourlife.entity.User;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GetUserInfoResponse {


    private Long id;

    private String nickname;

    @Column(unique = true)
    private String email;

    private String birth;

    private String introduce;

    private String profileImgUrl;

    public static GetUserInfoResponse from(User user) {
        return GetUserInfoResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .birth(user.getBirth())
                .introduce(user.getIntroduce())
                .profileImgUrl(user.getProfileImgUrl())
                .build();
    }
}
