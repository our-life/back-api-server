package com.ourlife.service.impl;

import com.ourlife.dto.user.SigninRequest;
import com.ourlife.entity.User;
import com.ourlife.exception.AccountNotFoundException;
import com.ourlife.exception.AccountPasswordMissmatchException;
import com.ourlife.exception.DuplicatedEmailException;
import com.ourlife.repository.UserRepository;
import com.ourlife.service.UserService;
import com.ourlife.utils.Impl.BcryptPasswordEncoder;
import com.ourlife.utils.Impl.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BcryptPasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    /**
     * @Return true 사용 가능한 이메일, false 사용 불가능한 이메일
     * */
    @Override
    public boolean validateDuplicationEmail(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Override
    public void signup(User user) {
        if (!validateDuplicationEmail(user.getEmail())) {
            throw new DuplicatedEmailException("이메일 중복 확인이 필요합니다.");
        }

        userRepository.save(user);
    }

    @Override
    public String signin(SigninRequest signinRequest) {

        User user = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(()-> new AccountNotFoundException("유저의 이메일이 없습니다."));

        if(!passwordEncoder.match(signinRequest.getPassword(), user.getPassword())){
            throw new AccountPasswordMissmatchException("비밀번호를 확인해주세요.");
        }
        return jwtTokenUtils.generateToken(user);
    }
}
