package com.ourlife.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity{

    @Id
    @Column(name = "commentId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private OurLife ourLife;

    @Column(name = "commentContents")
    private String contents;


    //TODO: createComment DTO 만들어서 추가
    private static Comment createComment(User user, OurLife ourLife){
        Comment comment = new Comment();
        comment.user = user;
        comment.ourLife = ourLife;

        return null;
    }

}
