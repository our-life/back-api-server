package com.ourlife.service;

import com.ourlife.dto.user.*;
import com.ourlife.entity.User;
import jakarta.servlet.ServletRequest;

import java.util.List;

public interface UserService {

    boolean validateDuplicationEmail(String email);

    UserResponse signup(User user);

    String signin(SigninRequest signinRequest);

    GetUserInfoResponse getUserInfo(String token);

    UserResponse updateUser(String token, UpdateUserRequest request);

    UserResponse deleteUser(String token);

    List<GetUserListResponse> getUserList(GetUserListRequest userListRequest, String token);

    UserResponse addFollow(FollowRequest followRequest, String token);

    Boolean validateFollow(User fromUser, User toUser);

    UserResponse deleteFollow(FollowRequest followRequest, String token);

    Object getFollower(String token);

    Object getFollowing(String token);
}
