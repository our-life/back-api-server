package com.ourlife.dto.user;


import com.ourlife.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
//@Builder
public class GetFollowingResponse {

    private List<String> followingUserEmail;


    public static GetFollowingResponse followerResponse(List<String> email){
        GetFollowingResponse response = new GetFollowingResponse();
        response.followingUserEmail = email;

        return response;
    }
}
