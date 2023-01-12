package com.ourlife.dto.user;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponse {

    private String message;

    public static UserResponse response(String result){
        return UserResponse.builder()
                .message(result)
                .build();
    }
}


