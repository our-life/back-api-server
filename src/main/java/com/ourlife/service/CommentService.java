package com.ourlife.service;

import com.ourlife.dto.comment.CommentResponse;
import com.ourlife.dto.comment.CreateCommentRequest;
import com.ourlife.dto.comment.UpdateCommentRequest;

public interface CommentService {

    public CommentResponse createComment(CreateCommentRequest request, String token);

    public CommentResponse updataComment(UpdateCommentRequest request, String token);


}
