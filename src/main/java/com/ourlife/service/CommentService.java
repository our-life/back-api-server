package com.ourlife.service;

import com.ourlife.dto.comment.*;

public interface CommentService {

    public CommentResponse createComment(CreateCommentRequest request, String token);

    public CommentResponse updataComment(UpdateCommentRequest request, String token);

    public CommentResponse deleteComment(DeleteCommentRequest request, String token);

    public CommentResponse likeComment(LikeCommentRequest request, String token);

    public CommentResponse unlikeComment(LikeCommentRequest request, String token);

}
