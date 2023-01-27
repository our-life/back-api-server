package com.ourlife.service.impl;

import com.ourlife.dto.comment.*;
import com.ourlife.entity.Comment;
import com.ourlife.entity.CommentLike;
import com.ourlife.entity.OurLife;
import com.ourlife.entity.User;
import com.ourlife.exception.OurLifeNotFoundException;
import com.ourlife.exception.UserNotFoundException;
import com.ourlife.repository.CommentLikeRepository;
import com.ourlife.repository.CommentRepository;
import com.ourlife.repository.OurlifeRepository;
import com.ourlife.repository.UserRepository;
import com.ourlife.service.CommentService;
import com.ourlife.utils.Impl.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;
    private final OurlifeRepository ourlifeRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    //http://localhost:8080/h2-console/


    @Override
    public List<GetCommentListResponse> getCommentList(GetCommentListRequest request, String token) {
        User user = parseJwtToken(token);

        OurLife ourLife = ourlifeRepository.findById(request.getAraId())
                .orElseThrow(() -> new OurLifeNotFoundException("해당 글이 없습니다"));

        List<Comment> comments = commentRepository.findByOurLifeId(ourLife.getId());
        List<GetCommentListResponse> responses = new ArrayList<>();

        if(comments.isEmpty()) return null;

        for (Comment comment : comments) {
            int likeCount = commentLikeRepository.countByCommentId(comment.getId());
            responses.add(GetCommentListResponse.from(user, comment, likeCount));
        }

        return responses;
    }

    @Override
    public CommentResponse createComment(CreateCommentRequest request, String token) {
        User user = parseJwtToken(token);

        OurLife ourLife = ourlifeRepository.findById(request.getAraId())
                .orElseThrow(() -> new OurLifeNotFoundException("해당 글이 없습니다"));

        Comment comment = Comment.createComment(user, ourLife, request.getContents());

        ourlifeRepository.save(OurLife.commentOurlife(ourLife, comment));

        return CommentResponse.response("성공");
    }

    @Override
    public CommentResponse updataComment(UpdateCommentRequest request, String token) {
        User user = parseJwtToken(token);

        Comment comment = commentRepository.findByOurLifeIdAndId(request.getAraId(), request.getCommentId())
                .orElseThrow(() -> new OurLifeNotFoundException("댓글을 찾을 수 없습니다."));

        if (!user.getId().equals(comment.getUser().getId())) throw new UserNotFoundException("본인의 댓글이 아닙니다");

        OurLife ourLife = ourlifeRepository.findById(comment.getOurLife().getId())
                .orElseThrow(() -> new OurLifeNotFoundException("해당 글이 없습니다"));

        ourLife.getCommentList().remove(comment);

        Comment.updateCommnet(comment, request.getContents());
        ourlifeRepository.save(OurLife.commentOurlife(ourLife, comment));

        return CommentResponse.response("성공");
    }

    @Override
    public CommentResponse deleteComment(DeleteCommentRequest request, String token) {
        User user = parseJwtToken(token);

        OurLife ourLife = ourlifeRepository.findById(request.getAraId())
                .orElseThrow(() -> new OurLifeNotFoundException("해당 글이 없습니다"));

        Comment comment = commentRepository.findByOurLifeIdAndId(ourLife.getId(), request.getCommentId())
                .orElseThrow(() -> new OurLifeNotFoundException("댓글을 찾을 수 없습니다."));

        if(!user.getId().equals(comment.getUser().getId())) throw new UserNotFoundException("본인의 댓글이 아닙니다");

        ourLife.getCommentList().remove(comment);
        ourlifeRepository.save(ourLife);

        return CommentResponse.response("성공");
    }

    @Override
    public CommentResponse likeComment(LikeCommentRequest request, String token) {
        User user = parseJwtToken(token);

        OurLife ourLife = ourlifeRepository.findById(request.getAraId())
                .orElseThrow(() -> new OurLifeNotFoundException("해당 글이 없습니다"));

        Comment comment = commentRepository.findByOurLifeIdAndId(request.getAraId(), request.getCommentId())
                .orElseThrow(() -> new OurLifeNotFoundException("댓글을 찾을 수 없습니다."));

        if(commentLikeRepository.existsByCommentIdAndUserId(user.getId(), comment.getId())){
            throw new OurLifeNotFoundException("이미 좋아요를 누르셨습니다.");
        }

        CommentLike commentLike = CommentLike.createCommentLike(user, comment);

        commentRepository.save(Comment.commentAddLike(comment, commentLike));

        return CommentResponse.response("성공");
    }

    @Override
    public CommentResponse unlikeComment(LikeCommentRequest request, String token) {
        User user = parseJwtToken(token);

        OurLife ourLife = ourlifeRepository.findById(request.getAraId())
                .orElseThrow(() -> new OurLifeNotFoundException("해당 글이 없습니다"));

        Comment comment = commentRepository.findByOurLifeIdAndId(request.getAraId(), request.getCommentId())
                .orElseThrow(() -> new OurLifeNotFoundException("댓글을 찾을 수 없습니다."));

        CommentLike commentLike = commentLikeRepository.findByCommentIdAndUserId(comment.getId(), user.getId())
                .orElseThrow(() -> new OurLifeNotFoundException("좋아요를 누르시지 않았습니다"));

        comment.getCommentLikes().remove(commentLike);

        commentRepository.save(comment);

        return CommentResponse.response("성공");
    }

    //공통 메서드
    private User parseJwtToken(String token) {
        Long userId = jwtTokenUtils.parseUserIdFrom(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유효하지 않은 토큰입니다."));

        return user;
    }
}
