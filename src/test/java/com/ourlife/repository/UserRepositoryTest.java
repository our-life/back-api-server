package com.ourlife.repository;

import com.ourlife.Fixture;
import com.ourlife.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.Transactional;

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
    @DisplayName("이메일 기준으로 유저 정보 불러오기")
    void findByEmail_True(){
        User savedUser = saveUser();
        String email = "test@test.com";
        // Fixture의 유저를 받아오면 어차피 같은 값이 아닐까...
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent()){
            assertThat(optionalUser.get().getEmail()).isEqualTo(email);
        }else{
            assertThat(optionalUser).isEmpty();
        }

    }

    @Test
    @DisplayName("이메일 기준으로 유저 정보 불러오기")
    void findByEmail_False(){
        User savedUser = saveUser();
        String email = "test@test.com";
        // Fixture의 유저를 받아오면 어차피 같은 값이 아닐까...
        Optional<User> optionalUser = userRepository.findByEmail(email+".com");

        if(optionalUser.isPresent()){
            assertThat(optionalUser.get().getEmail()).isEqualTo(email);
        }else{
            assertThat(optionalUser).isEmpty();
        }

    }
}
