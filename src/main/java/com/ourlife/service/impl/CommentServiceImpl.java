package com.ourlife.service.impl;

import com.ourlife.dto.comment.CommentResponse;
import com.ourlife.dto.comment.CreateCommentRequest;
import com.ourlife.dto.comment.DeleteCommentRequest;
import com.ourlife.dto.comment.UpdateCommentRequest;
import com.ourlife.entity.Comment;
import com.ourlife.entity.OurLife;
import com.ourlife.entity.User;
import com.ourlife.exception.OurLifeNotFoundException;
import com.ourlife.exception.UserNotFoundException;
import com.ourlife.repository.CommentRepository;
import com.ourlife.repository.OurlifeRepository;
import com.ourlife.repository.UserRepository;
import com.ourlife.service.CommentService;
import com.ourlife.utils.Impl.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;
    private final OurlifeRepository ourlifeRepository;
    private final CommentRepository commentRepository;

    //http://localhost:8080/h2-console/

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

        Comment comment = commentRepository.findCommentByOurLifeIdAndId(request.getAraId(), request.getCommentId());

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

        Comment comment = commentRepository.findCommentByOurLifeIdAndId(ourLife.getId(), request.getCommentId());

        if(!user.getId().equals(comment.getUser().getId())) throw new UserNotFoundException("본인의 댓글이 아닙니다");

        ourLife.getCommentList().remove(comment);
        ourlifeRepository.save(ourLife);

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
