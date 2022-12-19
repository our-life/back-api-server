package com.ourlife.dto.user;

import lombok.Data;
import lombok.Getter;

@Getter
public class SigninRequest {

    private String email;

    private String password;
}
