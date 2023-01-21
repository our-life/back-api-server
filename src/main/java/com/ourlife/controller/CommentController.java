package com.ourlife.controller;

import com.ourlife.argumentResolver.ValidateToken;
import com.ourlife.dto.comment.CommentResponse;
import com.ourlife.dto.comment.CreateCommentRequest;
import com.ourlife.dto.comment.UpdateCommentRequest;
import com.ourlife.entity.Comment;
import com.ourlife.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CreateCommentRequest request,
                                                         @ValidateToken String token) {
        return ResponseEntity.ok().body(commentService.createComment(request, token));
    }


    @PatchMapping("/comments")
    public ResponseEntity<CommentResponse> updateComment(@RequestBody UpdateCommentRequest request,
                                                         @ValidateToken String token){
        return ResponseEntity.ok().body(commentService.updataComment(request, token));
    }
}
