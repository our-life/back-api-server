package com.ourlife.dto.user;

import com.ourlife.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetUserListResponse {

    private List<String> nickname;


    public static GetUserListResponse response(List<String> userNickNameList){
        return GetUserListResponse.builder()
                .nickname(userNickNameList)
                .build();
    }


}
