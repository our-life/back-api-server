package com.ourlife.repository;

import com.ourlife.Fixture;
import com.ourlife.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@PropertySource(value = "classpath:/application-test.properties")
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    public User saveUser() {
        User user = Fixture.user();
        userRepository.save(user);
        return user;
    }

    public User saveUser1() {
        User user = Fixture.user1();
        userRepository.save(user);
        return user;
    }

    @Test
    @DisplayName("이메일 중복 확인 true (이미 가입된 이메일)")
    void existsByEmail_True() {
        User savedUser = saveUser();
        boolean result = userRepository.existsByEmail(savedUser.getEmail());
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("이메일 중복 확인 false (사용할 수 있는 이메일)")
    void existsByEmail_False() {
        boolean result = userRepository.existsByEmail(Fixture.user().getEmail());
        assertThat(result).isFalse();
    }


    @Test
    @DisplayName("이메일 기준으로 유저 정보 불러오기_성공")
    void findByEmail_True() {
        User savedUser = saveUser();
        String email = "test@test.com";
        // Fixture의 유저를 받아오면 어차피 같은 값이 아닐까...
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            assertThat(optionalUser.get().getEmail()).isEqualTo(email);
        } else {
            assertThat(optionalUser).isEmpty();
        }

    }

    @Test
    @DisplayName("이메일 기준으로 유저 정보 불러오기_실패")
    void findByEmail_False() {
        User savedUser = saveUser();
        String email = "test@test.com";
        // Fixture의 유저를 받아오면 어차피 같은 값이 아닐까...
        Optional<User> optionalUser = userRepository.findByEmail(email + ".com");

        if (optionalUser.isPresent()) {
            assertThat(optionalUser.get().getEmail()).isEqualTo(email);
        } else {
            assertThat(optionalUser).isEmpty();
        }

    }

    @Test
    @DisplayName("문자열로 시작하는 닉네임 가져오기")
    void findByNicknameStartingWith_true() {
        User savadUser1 = saveUser();
        User savedUser2 = saveUser1();

        List<User> names = userRepository.findByNicknameStartingWith("te");

        assertThat(names.get(0).getNickname()).isEqualTo(savadUser1.getNickname());
        assertThat(names.get(1).getNickname()).isEqualTo(savedUser2.getNickname());
    }

    @Test
    @DisplayName("문자열로 시작하는 닉네임 가져오기 (검색하는 닉네임이 없는 경우)")
    void findByNicknameStartingWith_false() {
        User savadUser1 = saveUser();

        List<User> names = userRepository.findByNicknameStartingWith("hwan");

        assertThat(names).isEmpty();
    }
}
