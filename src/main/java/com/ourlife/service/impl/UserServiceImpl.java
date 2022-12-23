package com.ourlife.service.impl;

import com.ourlife.dto.user.SigninRequest;
import com.ourlife.entity.User;
import com.ourlife.exception.AccountNotFoundException;
import com.ourlife.exception.AccountPasswordMissmatchException;
import com.ourlife.exception.DuplicatedEmailException;
import com.ourlife.repository.UserRepository;
import com.ourlife.service.UserService;
import com.ourlife.utils.Impl.BcryptPasswordEncoder;
import com.ourlife.utils.Impl.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BcryptPasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    /**
     * @Return true 사용 가능한 이메일, false 사용 불가능한 이메일
     */
    @Override
    public boolean validateDuplicationEmail(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Override
    public void signup(User user) {
        if (!validateDuplicationEmail(user.getEmail())) {
            throw new DuplicatedEmailException("이메일 중복 확인이 필요합니다.");
        }

        userRepository.save(user);
    }

    @Override
    public String signin(SigninRequest signinRequest) {
        User user = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("유저의 이메일이 없습니다."));

        if (!passwordEncoder.match(signinRequest.getPassword(), user.getPassword())) {
            throw new UserPasswordMissmatchException("비밀번호를 확인해주세요.");
        }

        return jwtTokenUtils.generateAccessToken(user);
    }

    @Override
    public GetUserInfoResponse getUserInfo(String token) {
        if (!jwtTokenUtils.validateToken(token)) {
            throw new IllegalStateException("유효하지 않은 토큰입니다.");
        }

        Long userId = jwtTokenUtils.parseUserIdFrom(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저 입니다."));

        return GetUserInfoResponse.from(user);
    }

    @Transactional
    @Override
    public void updateUser(String token, UpdateUserRequest request) {
        if (!jwtTokenUtils.validateToken(token)) {
            throw new IllegalStateException("유효하지 않은 토큰입니다.");
        }

        Long userId = jwtTokenUtils.parseUserIdFrom(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저 입니다."));

        user.update(request);
    }

    @Transactional
    @Override
    public void deleteUser(String token) {
        if (!jwtTokenUtils.validateToken(token)) {
            throw new IllegalStateException("유효하지 않은 토큰입니다.");
        }

        Long userId = jwtTokenUtils.parseUserIdFrom(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저 입니다."));

        userRepository.delete(user);
    }

    //@Transactional(rollbackOn = Exception.class)
    @Override
    public void addFollow(FollowRequest followRequest, ServletRequest request) {

        String token = jwtTokenUtils.resolveToken((HttpServletRequest) request);
        Long userId = jwtTokenUtils.parseUserIdFrom(token);

        //toUserEmail 과  token userId (from)
        //밑의 예외 처리는 작동 안함. token -> userId 추출시 예외처리 필요
        User fromUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저의 아이디가 없습니다."));

        User toUser = userRepository.findByEmail(followRequest.getToUserEmail())
                .orElseThrow(() -> new UserNotFoundException("상대의 유저의 아이디가 없습니다."));

        boolean validateFollow = validateFollow(fromUser, toUser);
        if (validateFollow) {
            throw new FollowMissMatchException("이미 팔로우를 하셨습니다");
        }

        Follow follow = Follow.createFollow(fromUser, toUser);

        followRepository.save(follow);
    }

    @Override
    public Boolean validateFollow(User fromUser, User toUser) {
        boolean result = false;
        Optional<Follow> optionalFollow = followRepository.findByFromUserAndToUser(fromUser,toUser);

        if (optionalFollow.isPresent()) {
            Follow follow = optionalFollow.get();
            if (follow.getToUser() == toUser) {
                result = true;
            }
        }
        // 더 좋은 코드 고민 validateFollow
        //Optional 쓴 김에 간결하게 짜면 좋을거 같은데
        return result;
    }

    @Override
    public void deleteFollow(FollowRequest followRequest, ServletRequest request) {
        String token = jwtTokenUtils.resolveToken((HttpServletRequest) request);
        Long userId = jwtTokenUtils.parseUserIdFrom(token);

        //toUserEmail 과  token userId (from)
        User fromUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저의 아이디가 없습니다."));

        User toUser = userRepository.findByEmail(followRequest.getToUserEmail())
                .orElseThrow(() -> new UserNotFoundException("상대의 유저의 아이디가 없습니다."));

        Follow follow = followRepository.findByFromUserAndToUser(fromUser, toUser)
                .orElseThrow(() -> new FollowMissMatchException("팔로우하지 않았습니다."));

        followRepository.delete(follow);
    }

    @Override
    public Object getFollower(ServletRequest request) {
        String token = jwtTokenUtils.resolveToken((HttpServletRequest) request);
        Long userId = jwtTokenUtils.parseUserIdFrom(token);

        User fromUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저의 아이디가 없습니다."));

        //ToUser 로 조회.
        List<Follow> toUserFollowList = followRepository.findAllByToUser(fromUser);
        List<GetFollowerResponse> getFollowers = new ArrayList<>();
        HashMap<String, Object> response = new HashMap<>();
        for (int i = 0; i <toUserFollowList.size() ; i++) {
            //toUser 의 fromUser 를 조회함
            getFollowers.add(GetFollowerResponse.followerResponse(toUserFollowList.get(i).getFromUser().getEmail()));
        }
        response.put("followers", getFollowers);

        return response;
    }

    @Override
    public Object getFollowing(ServletRequest request) {
        String token = jwtTokenUtils.resolveToken((HttpServletRequest) request);
        Long userId = jwtTokenUtils.parseUserIdFrom(token);

        User fromUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저의 아이디가 없습니다."));

        List<Follow> followList =  followRepository.findAllByFromUser(fromUser);

        List<GetFollowingResponse> getFollowings = new ArrayList<>();
        HashMap<String, Object> response = new HashMap<>();
        for (int i = 0; i <followList.size() ; i++) {
            getFollowings.add(GetFollowingResponse.followerResponse(followList.get(i).getToUser().getEmail()));
        }

        response.put("following", getFollowings);

        return response;
    }
}
