package com.ourlife.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    List<CommentLike> commentLikes = new ArrayList<>();


    public static Comment createComment(User user, OurLife ourLife, String contents){
        Comment comment = new Comment();
        comment.user = user;
        comment.ourLife = ourLife;
        comment.contents = contents;
        return comment;
    }

    public static Comment updateCommnet(Comment comment, String contents){
        comment.contents = contents;
        return comment;
    }

    public static Comment commentAddLike(Comment comment, CommentLike commentLike){
        comment.commentLikes.add(commentLike);
        return comment;
    }
}
