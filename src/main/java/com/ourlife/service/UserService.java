package com.ourlife.service;

import com.ourlife.dto.user.*;
import com.ourlife.entity.User;

import java.util.List;

public interface UserService {

    boolean validateDuplicationEmail(String email);

    UserWithInfoResponse signup(User user);

    Object[] signin(SigninRequest signinRequest);

    GetUserInfoResponse getUserInfo(String token);

    UserWithInfoResponse updateUser(String token, UpdateUserRequest request);

    UserWithInfoResponse deleteUser(String token);

    List<GetUserListResponse> getUserList(GetUserListRequest userListRequest, String token);

    UserWithInfoResponse addFollow(FollowRequest followRequest, String token);

    Boolean validateFollow(User fromUser, User toUser);

    UserWithInfoResponse deleteFollow(FollowRequest followRequest, String token);

    Object getFollower(String token);

    Object getFollowing(String token);
}
