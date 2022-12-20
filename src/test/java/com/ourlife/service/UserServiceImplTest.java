package com.ourlife.service;

import com.ourlife.Fixture;
import com.ourlife.dto.user.GetUserInfoResponse;
import com.ourlife.entity.User;
import com.ourlife.exception.UserNotFoundException;
import com.ourlife.exception.DuplicatedEmailException;
import com.ourlife.repository.UserRepository;
import com.ourlife.service.impl.UserServiceImpl;
import com.ourlife.utils.Impl.JwtTokenUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class
UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    JwtTokenUtils jwtTokenUtils;

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

//    @Test
//    @DisplayName("로그인 성공")
//    void signin_success(){
//        User user = Fixture.user();
//        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
//
//    }

    @Test
    @DisplayName("회원정보 조회 성공")
    void getUserInfo_success() {
        User user = Fixture.user(1L);
        given(jwtTokenUtils.validateToken(anyString())).willReturn(true);
        given(jwtTokenUtils.parseUserIdFrom(anyString())).willReturn(user.getId());
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        GetUserInfoResponse res = userService.getUserInfo(anyString());

        assertThat(res.getId()).isEqualTo(user.getId());
        assertThat(res.getBirth()).isEqualTo(user.getBirth());
        assertThat(res.getIntroduce()).isEqualTo(user.getIntroduce());
        assertThat(res.getNickname()).isEqualTo(user.getNickname());
        assertThat(res.getProfileImgUrl()).isEqualTo(user.getProfileImgUrl());
    }

    @Test
    @DisplayName("회원정보 조회 실패 invalid token")
    void getUserInfo_fail_invalid_token() {
        given(jwtTokenUtils.validateToken(anyString())).willReturn(false);

        Assertions.assertThrows(IllegalStateException.class,
                () -> userService.getUserInfo(anyString()));
    }

    @Test
    @DisplayName("회원정보 조회 실패 user_not_found")
    void getUserInfo_fail_user_not_found() {
        User user = Fixture.user(1L);
        given(jwtTokenUtils.validateToken(anyString())).willReturn(true);
        given(jwtTokenUtils.parseUserIdFrom(anyString())).willReturn(user.getId());
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.getUserInfo(anyString()));
    }

    @Test
    @DisplayName("회원정보 수정")
    void updateUser() {
        User user = Fixture.user(1L);
        given(jwtTokenUtils.validateToken(anyString())).willReturn(true);
        given(jwtTokenUtils.parseUserIdFrom(anyString())).willReturn(user.getId());
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        Assertions.assertDoesNotThrow(() ->
                userService.updateUser(Fixture.token(), Fixture.updateUserRequest()));
    }

    @Test
    @DisplayName("회원정보 수정 유효하지 않은 토큰")
    void updateUser_fail_invalid_token() {
        given(jwtTokenUtils.validateToken(anyString())).willReturn(false);

        Assertions.assertThrows(IllegalStateException.class,
                () -> userService.updateUser(Fixture.token(), Fixture.updateUserRequest()));
    }

    @Test
    @DisplayName("회원정보 수정 존재 하지 않는 회원")
    void updateUser_fail_user_not_found() {
        User user = Fixture.user(1L);
        given(jwtTokenUtils.validateToken(anyString())).willReturn(true);
        given(jwtTokenUtils.parseUserIdFrom(anyString())).willReturn(user.getId());
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(Fixture.token(), Fixture.updateUserRequest()));
    }

    @Test
    @DisplayName("회원정보 삭제")
    void deleteUser() {
        User user = Fixture.user(1L);
        given(jwtTokenUtils.validateToken(anyString())).willReturn(true);
        given(jwtTokenUtils.parseUserIdFrom(anyString())).willReturn(user.getId());
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        Assertions.assertDoesNotThrow(() ->
                userService.deleteUser(Fixture.token()));
    }

    @Test
    @DisplayName("회원정보 삭제 유효하지 않은 토큰")
    void deleteUser_fail_invalid_token() {
        given(jwtTokenUtils.validateToken(anyString())).willReturn(false);

        Assertions.assertThrows(IllegalStateException.class,
                () -> userService.deleteUser(Fixture.token()));
    }

    @Test
    @DisplayName("회원정보 삭제 존재하지 않는 회원")
    void deleteUser_fail_user_not_found() {
        User user = Fixture.user(1L);
        given(jwtTokenUtils.validateToken(anyString())).willReturn(true);
        given(jwtTokenUtils.parseUserIdFrom(anyString())).willReturn(user.getId());
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser(Fixture.token()));
    }
}
