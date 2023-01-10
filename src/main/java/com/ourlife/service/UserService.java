package com.ourlife.service;

import com.ourlife.dto.user.*;
import com.ourlife.entity.User;
import jakarta.servlet.ServletRequest;

import java.util.List;

public interface UserService {

    boolean validateDuplicationEmail(String email);

    void signup(User user);

    String signin(SigninRequest signinRequest);

    GetUserInfoResponse getUserInfo(String token);

    void updateUser(String token, UpdateUserRequest request);

    void deleteUser(String token);

    List<GetUserListResponse> getUserList(GetUserListRequest userListRequest, String token);

    void addFollow(FollowRequest followRequest, String token);

    Boolean validateFollow(User fromUser, User toUser);

    void deleteFollow(FollowRequest followRequest, String token);

    Object getFollower(String token);

    Object getFollowing(String token);
}
