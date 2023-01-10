package com.ourlife.dto.user;


import com.ourlife.entity.Follow;
import lombok.Data;

import java.util.List;

@Data
public class GetFollowerResponse {

    private String followerUserEmail;

    public static GetFollowerResponse followerResponse(String email){
        GetFollowerResponse response = new GetFollowerResponse();
        response.followerUserEmail = email;

        return response;
    }

    public static GetFollowerResponse followerResponse(List<String> email){
        GetFollowerResponse response = new GetFollowerResponse();
        response.followerUserEmail = email.toString();

        return response;
    }

}
