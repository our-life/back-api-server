package com.ourlife.utils;

public interface PasswordEncoder {

    String hash(String originPassword);

    boolean match(String origin, String hashed);
}
