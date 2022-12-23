package com.ourlife.service;

import com.ourlife.Fixture;
import com.ourlife.entity.User;
import com.ourlife.exception.DuplicatedEmailException;
import com.ourlife.exception.UserNotFoundException;
import com.ourlife.exception.UserPasswordMissmatchException;
import com.ourlife.repository.UserRepository;
import com.ourlife.service.impl.UserServiceImpl;
import com.ourlife.utils.Impl.BcryptPasswordEncoder;
import com.ourlife.utils.Impl.JwtTokenUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class
UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    BcryptPasswordEncoder passwordEncoder;
    @Mock
    JwtTokenUtils tokenUtils;
    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("사용 가능한 이메일이면 true를 반환한다.")
    void validateDuplicationEmailTest_true() {
        String email = anyString();
        given(userRepository.existsByEmail(email)).willReturn(false);
        boolean result = userService.validateDuplicationEmail(email);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("사용 불가능한 이메일이면 false를 반환한다.")
    void validateDuplicationEmailTest_false() {
        String email = anyString();
        given(userRepository.existsByEmail(email)).willReturn(true);
        boolean result = userService.validateDuplicationEmail(email);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() {
        User user = Fixture.user();
        Assertions.assertDoesNotThrow(() -> userService.signup(user));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 이메일")
    void signup_fail() {
        User user = Fixture.user();
        given(userRepository.existsByEmail(user.getEmail())).willReturn(true);
        Assertions.assertThrows(DuplicatedEmailException.class, () -> userService.signup(user));
    }

    @Test
    @DisplayName("로그인 성공")
    void signin_success() {

        User user = Fixture.user();

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.match(eq(user.getPassword()), anyString())).willReturn(true);
        given(tokenUtils.generateAccessToken(user)).willReturn(anyString());

        Assertions.assertDoesNotThrow(() -> userService.signin(Fixture.signinRequest()));
/*
        1. 유저가 저장 되어있어야 한다
         2. Request 값이 들어옴
         3. 이메일 기준으로 유저 값을 찾고
         4. 입력값과 유저의 db password 맞는지 확인
         5. token 이 null 아닌지 확인
         이게 맞나...?
         */
    }
    @Test
    @DisplayName("로그인 실패_비밀번호 다름")
    void signin_False_UserPasswordMissmatch() {
        User user = Fixture.user();
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        UserPasswordMissmatchException exception = Assertions.assertThrows(UserPasswordMissmatchException.class,
                () -> userService.signin(Fixture.signinRequest()));

    }
    @Test
    @DisplayName("로그인 실패_유저 없음")
    void signin_False_UserNotFound() {
        User user = Fixture.user();
        //given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.signin(Fixture.signinRequest()));

    }
}
