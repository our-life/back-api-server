package com.ourlife.utils.Impl;

import com.ourlife.utils.PasswordEncoder;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BcryptPasswordEncoder implements PasswordEncoder {

    @Override
    public String hash(String originPassword) {
        return BCrypt.hashpw(originPassword, BCrypt.gensalt());
    }

    @Override
    public boolean match(String origin, String hashed) {
        return BCrypt.checkpw(origin, hashed);
    }
}
