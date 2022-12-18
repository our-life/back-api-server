package com.ourlife.service;

import com.ourlife.entity.User;

public interface UserService {

    boolean validateDuplicationEmail(String email);

    void signup(User user);
}
