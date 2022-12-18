package com.ourlife.service.impl;

import com.ourlife.repository.UserRepository;
import com.ourlife.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * @Return true 사용 가능한 이메일, false 사용 불가능한 이메일
     * */
    @Override
    public boolean validateDuplicationEmail(String email) {
        return !userRepository.existsByEmail(email);
    }
}
