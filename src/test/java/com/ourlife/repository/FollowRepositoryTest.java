package com.ourlife.repository;

import com.ourlife.Fixture;
import com.ourlife.entity.Follow;
import com.ourlife.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class FollowRepositoryTest {

    @Autowired
    FollowRepository followRepository;

    @Test
    @DisplayName("Follow 정보를 저장합니다.")
    public Follow save(){
        User fromUser = Fixture.user(1);
        User toUser = Fixture.user(2);
        Follow save = followRepository.save(Follow.createFollow(fromUser, toUser));
        //?
        return save;
    }

    @Test
    void findByFromUser() {
    }

    @Test
    @DisplayName("fromUser와 toUser로 조회")
    void findByFromUserAndToUser() {
        User fromUser = Fixture.user();
        User toUser = Fixture.user();

    }

    @Test
    void findAllByFromUser() {
    }

    @Test
    void findByToUser() {
    }
}