package com.ourlife.dto.user;


import com.ourlife.entity.Follow;
import io.jsonwebtoken.lang.Collections;
import lombok.Data;

import java.util.*;

@Data
public class GetFollowerResponse {

    private List<String> followerUserEmail;


    public static GetFollowerResponse followerResponse(List<String> email){
        GetFollowerResponse response = new GetFollowerResponse();
        response.followerUserEmail = email;

        return response;
    }

}
