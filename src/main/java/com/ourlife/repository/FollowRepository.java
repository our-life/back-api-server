package com.ourlife.repository;


import com.ourlife.entity.Follow;
import com.ourlife.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFromUser(User fromUser);

    Optional<Follow> findByFromUserAndToUser(User fromUser, User toUser);

    List<Follow> findAllByFromUser(User fromUser);

    List<Follow> findAllByToUser(User toUser);

}
