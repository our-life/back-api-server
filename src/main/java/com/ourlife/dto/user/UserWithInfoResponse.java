package com.ourlife.dto.user;

import com.ourlife.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserWithInfoResponse {

    private String message;

    private String email;

    private String nickname;

    private String birth;

    public static UserWithInfoResponse response(String result, User user){
        return UserWithInfoResponse.builder()
                .message(result)
                .email(user.getEmail())
                .nickname(user.getNickname())
                .birth(user.getBirth())
                .build();
    }
}


