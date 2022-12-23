package com.ourlife.dto.user;


import lombok.Data;

@Data
public class GetFollowingResponse {

    private String followingUserEmail;

    public static GetFollowingResponse followerResponse(String email){
        GetFollowingResponse response = new GetFollowingResponse();
        response.followingUserEmail = email;

        return response;
    }

}
