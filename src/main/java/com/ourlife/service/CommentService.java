package com.ourlife.service;

import com.ourlife.dto.comment.CommentResponse;
import com.ourlife.dto.comment.CreateCommentRequest;

public interface CommentService {

    public CommentResponse createComment(CreateCommentRequest request, String token);

}
