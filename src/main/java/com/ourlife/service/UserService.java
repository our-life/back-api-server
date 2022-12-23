package com.ourlife.service;

import com.ourlife.dto.user.FollowRequest;
import com.ourlife.dto.user.GetFollowerResponse;
import com.ourlife.dto.user.SigninRequest;
import com.ourlife.entity.User;
import jakarta.servlet.ServletRequest;

import java.util.List;

public interface UserService {

    boolean validateDuplicationEmail(String email);

    void signup(User user);

    String signin(SigninRequest signinRequest);

    void addFollow(FollowRequest followRequest, ServletRequest request);

    Boolean validateFollow(User fromUser, User toUser);

    void deleteFollow(FollowRequest followRequest, ServletRequest request);

    Object getFollower(ServletRequest request);

    Object getFollowing(ServletRequest request);
}
