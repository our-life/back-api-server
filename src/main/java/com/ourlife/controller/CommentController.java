package com.ourlife.controller;

import com.ourlife.argumentResolver.ValidateToken;
import com.ourlife.dto.comment.*;
import com.ourlife.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comments")
    public ResponseEntity<List<GetCommentListResponse>> getCommentList(@RequestBody GetCommentListRequest request,
                                                                       @ValidateToken String token){
        return ResponseEntity.ok().body(commentService.getCommentList(request, token));
    }


    @PostMapping("/comments")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CreateCommentRequest request,
                                                         @ValidateToken String token) {
        return ResponseEntity.ok().body(commentService.createComment(request, token));
    }


    @PatchMapping("/comments")
    public ResponseEntity<CommentResponse> updateComment(@RequestBody UpdateCommentRequest request,
                                                         @ValidateToken String token) {
        return ResponseEntity.ok().body(commentService.updataComment(request, token));
    }

    @DeleteMapping("/comments")
    public ResponseEntity<CommentResponse> deleteComment(@RequestBody DeleteCommentRequest request,
                                                         @ValidateToken String token) {
        return ResponseEntity.ok().body(commentService.deleteComment(request, token));
    }

    @PostMapping("/comments/likes")
    public ResponseEntity<CommentResponse> likeComment(@RequestBody LikeCommentRequest request,
                                                       @ValidateToken String token) {
        return ResponseEntity.ok().body(commentService.likeComment(request, token));
    }

    @DeleteMapping("/comments/likes")
    public ResponseEntity<CommentResponse> unlikeComment(@RequestBody LikeCommentRequest request,
                                                         @ValidateToken String token){
        return ResponseEntity.ok().body(commentService.unlikeComment(request, token));
    }

}
