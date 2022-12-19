package com.ourlife.utils;

import com.ourlife.entity.User;

public interface TokenProvider {

    String generateAccessToken(User user);

    Long parseUserIdFrom(String token);

    boolean validateToken(String token);
}
