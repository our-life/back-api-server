package com.ourlife.repository;

import com.ourlife.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByOurLifeIdAndId(Long ourlifeId, Long commentId);

}
