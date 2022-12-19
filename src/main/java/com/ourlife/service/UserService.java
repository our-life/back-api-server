package com.ourlife.service;

import com.ourlife.dto.user.GetUserInfoResponse;
import com.ourlife.dto.user.SigninRequest;
import com.ourlife.dto.user.UpdateUserRequest;
import com.ourlife.entity.User;

public interface UserService {

    boolean validateDuplicationEmail(String email);

    void signup(User user);

    String signin(SigninRequest signinRequest);

    GetUserInfoResponse getUserInfo(String token);

    void updateUser(String token, UpdateUserRequest request);

    void deleteUser(String token);
}
