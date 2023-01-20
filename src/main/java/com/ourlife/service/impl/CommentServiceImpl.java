package com.ourlife.service.impl;

import com.ourlife.dto.comment.CommentResponse;
import com.ourlife.dto.comment.CreateCommentRequest;
import com.ourlife.dto.ourlife.OurlifeResponse;
import com.ourlife.dto.user.UserResponse;
import com.ourlife.entity.User;
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

    @Override
    public CommentResponse createComment(CreateCommentRequest request, String token) {
        User user = parseJwtToken(token);



        return null;
    }




    //공통 메서드
    private User parseJwtToken(String token){
        Long userId = jwtTokenUtils.parseUserIdFrom(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유효하지 않은 토큰입니다."));

        return user;
    }
}
