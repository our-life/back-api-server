package com.ourlife.service;

import com.ourlife.dto.user.SigninRequest;
import com.ourlife.entity.User;

public interface UserService {

    boolean validateDuplicationEmail(String email);

    void signup(User user);

    String signin(SigninRequest signinRequest);
}
