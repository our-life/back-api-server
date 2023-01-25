package com.ourlife.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentLike {

    @Id
    @Column(name = "comment_like_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Comment comment;


    public static CommentLike createCommentLike(User user, Comment comment){
        return CommentLike.builder()
                .user(user)
                .comment(comment)
                .build();
    }

}
