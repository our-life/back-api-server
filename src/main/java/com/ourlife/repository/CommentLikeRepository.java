package com.ourlife.repository;

import com.ourlife.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByCommentIdAndUserId(Long commentId, Long Id);

    boolean existsByCommentIdAndUserId(Long commentId, Long userId);

}
