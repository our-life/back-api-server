package com.ourlife.service;

import com.ourlife.repository.UserRepository;
import com.ourlife.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

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
}
