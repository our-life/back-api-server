package com.ourlife.repository;

import com.ourlife.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findCommentByOurLifeIdAndId(Long ourlifeId, Long UserId);

}
