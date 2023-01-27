package com.ourlife.dto.comment;

import com.ourlife.entity.Comment;
import com.ourlife.entity.User;
import lombok.*;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GetCommentListResponse {

    private String userEmail;

    private String contents;

    private int commentLikeCount;

    public static GetCommentListResponse from(User user, Comment comment, int likeCount) {
        return GetCommentListResponse.builder()
                .userEmail(user.getEmail())
                .contents(comment.getContents())
                .commentLikeCount(likeCount)
                .build();
    }

}
