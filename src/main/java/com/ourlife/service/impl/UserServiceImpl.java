package com.ourlife.service.impl;

import com.ourlife.dto.user.*;
import com.ourlife.entity.Follow;
import com.ourlife.entity.User;
import com.ourlife.exception.DuplicatedEmailException;
import com.ourlife.exception.FollowMissMatchException;
import com.ourlife.exception.UserNotFoundException;
import com.ourlife.exception.UserPasswordMissmatchException;
import com.ourlife.repository.FollowRepository;
import com.ourlife.repository.UserRepository;
import com.ourlife.service.UserService;
import com.ourlife.utils.Impl.BcryptPasswordEncoder;
import com.ourlife.utils.Impl.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BcryptPasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final FollowRepository followRepository;

    /**
     * @Return true 사용 가능한 이메일, false 사용 불가능한 이메일
     */
    @Override
    public boolean validateDuplicationEmail(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Override
    public UserWithInfoResponse signup(User user) {
        if (!validateDuplicationEmail(user.getEmail())) {
            throw new DuplicatedEmailException("이메일 중복 확인이 필요합니다.");
        }

        userRepository.save(user);

        return UserWithInfoResponse.response("성공", user);
    }

    @Override
    public Object[] signin(SigninRequest signinRequest) {
        User user = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("이메일을 확인해주세요."));

        if (!passwordEncoder.match(signinRequest.getPassword(), user.getPassword())) {
            throw new UserPasswordMissmatchException("비밀번호를 확인해주세요.");
        }

        Object[] responses = new Object[2];
        responses[0] =  jwtTokenUtils.generateAccessToken(user);
        responses[1] = user;
        return responses;
    }

    @Override
    public GetUserInfoResponse getUserInfo(String token) {
        Long userId = jwtTokenUtils.parseUserIdFrom(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저 입니다."));

        return GetUserInfoResponse.from(user);
    }

    @Transactional
    @Override
    public UserWithInfoResponse updateUser(String token, UpdateUserRequest request) {
        Long userId = jwtTokenUtils.parseUserIdFrom(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저 입니다."));

        user.update(request);

        return UserWithInfoResponse.response("성공", user);
    }

    @Transactional
    @Override
    public UserWithInfoResponse deleteUser(String token) {
        Long userId = jwtTokenUtils.parseUserIdFrom(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저 입니다."));

        userRepository.delete(user);

        return UserWithInfoResponse.response("성공", user);
    }
    @Transactional
    @Override
    public List<GetUserListResponse> getUserList(GetUserListRequest userListRequest,  String token) {
        List<User> userList = userRepository.findByNicknameStartingWith(userListRequest.getUserNickName());
        List<String> userNickName = new ArrayList<>();
        List<GetUserListResponse> responseUserList = new ArrayList<>();

        if(userList.isEmpty()){
            return responseUserList;
        }
        for (User user: userList) {
           userNickName.add(user.getNickname());
        }
        responseUserList.add(GetUserListResponse.response(userNickName));

        return responseUserList;
    }



    @Transactional
    @Override
    public UserWithInfoResponse addFollow(FollowRequest followRequest, String token) {
        Long userId = jwtTokenUtils.parseUserIdFrom(token);

        //toUserEmail 과  token userId (from)
        //밑의 예외 처리는 작동 안함. token -> userId 추출시 예외처리 필요
        User fromUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저의 아이디가 없습니다."));

        User toUser = userRepository.findByEmail(followRequest.getToUserEmail())
                .orElseThrow(() -> new UserNotFoundException("상대의 유저의 아이디가 없습니다."));

        if(fromUser.getEmail().equals(toUser.getEmail())){
            throw new FollowMissMatchException("본인을 팔로우 할 수 없습니다.");
        }

        if (validateFollow(fromUser, toUser)) {
            throw new FollowMissMatchException("이미 팔로잉 하셨습니다");
        }

        Follow follow = Follow.createFollow(fromUser, toUser);

        followRepository.save(follow);

        return UserWithInfoResponse.response("성공", fromUser);
    }
    @Transactional
    @Override
    public Boolean validateFollow(User fromUser, User toUser) {
        boolean result = false;
        Optional<Follow> optionalFollow = followRepository.findByFromUserAndToUser(fromUser,toUser);

        // 더 좋은 코드 고민 validateFollow
        //Optional 쓴 김에 간결하게 짜면 좋을거 같은데
        if (optionalFollow.isPresent()) {
            Follow follow = optionalFollow.get();
            if (follow.getToUser() == toUser) {
                result = true;
            }
        }
        return result;
    }
    @Transactional
    @Override
    public UserWithInfoResponse deleteFollow(FollowRequest followRequest, String token) {
        Long userId = jwtTokenUtils.parseUserIdFrom(token);

        //toUserEmail 과  token userId (from)
        User fromUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저의 아이디가 없습니다."));

        User toUser = userRepository.findByEmail(followRequest.getToUserEmail())
                .orElseThrow(() -> new UserNotFoundException("상대의 유저의 아이디가 없습니다."));

        Follow follow = followRepository.findByFromUserAndToUser(fromUser, toUser)
                .orElseThrow(() -> new FollowMissMatchException("팔로우하지 않았습니다."));

        followRepository.delete(follow);

        return UserWithInfoResponse.response("성공", fromUser);
    }
    @Transactional
    @Override
    public Object getFollower(String token) {
        Long userId = jwtTokenUtils.parseUserIdFrom(token);
        User fromUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 정보가 없습니다."));

        //ToUser 로 조회.
        List<Follow> toUserFollowList = followRepository.findAllByToUser(fromUser);
        List<String> userEmailList = new ArrayList<>();
        List<GetFollowerResponse> getFollowers = new ArrayList<>();

        for (Follow follow: toUserFollowList) {
            userEmailList.add(follow.getFromUser().getEmail());
        }

        getFollowers.add(GetFollowerResponse.followerResponse(userEmailList));

        return getFollowers;
    }
    @Transactional
    @Override
    public Object getFollowing(String token) {
        Long userId = jwtTokenUtils.parseUserIdFrom(token);

        User fromUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 정보가 없습니다."));

        List<Follow> followList =  followRepository.findAllByFromUser(fromUser);
        List<String> userEmailList = new ArrayList<>();
        List<GetFollowingResponse> getFollowings = new ArrayList<>();

        for (Follow follow: followList) {
            userEmailList.add(follow.getToUser().getEmail());
        }

        getFollowings.add(GetFollowingResponse.followerResponse(userEmailList));

        return getFollowings;
    }
        //필터, aop, 인터셉터, argument(추천)



}
