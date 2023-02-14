package com.ourlife.controller;

import com.ourlife.argumentResolver.ValidateToken;
import com.ourlife.dto.user.*;
import com.ourlife.entity.User;
import com.ourlife.service.UserService;
import com.ourlife.utils.PasswordEncoder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(name = "유저", description = "유저 관련 api 입니다.")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "이메일 중복 검사", description = "이메일 중복 검사입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content),
            @ApiResponse(responseCode = "409", description = "이메일 중복")
    })
    @GetMapping("/users/{email}/validation")
    public ResponseEntity<UserResponse> validationDuplicationEmail(@PathVariable("email") String email) {
        boolean result = userService.validateDuplicationEmail(email);
        return result ?
                ResponseEntity.ok().body(UserResponse.response("성공")) : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @Operation(summary = "화원가입", description = "회원가입 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(responseCode = "400", description = "입력값 오류"),
            @ApiResponse(responseCode = "409", description = "이메일 중복 확인 필요")
    })
    @PostMapping("/users")
    public ResponseEntity<UserWithInfoResponse> signup(@RequestBody SignupRequest signupRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.signup(signupRequest.toEntity(passwordEncoder)));
    }

    @Operation(summary = "로그인", description = "로그인 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "비밀번호를 확인해주세요"),
            @ApiResponse(responseCode = "404", description = "이메일을 확인해주세요")
    })
    @PostMapping("/users/signin")
    public ResponseEntity<?> signin(@RequestBody SigninRequest signinRequest) {
        Object[] responses = userService.signin(signinRequest);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + responses[0])
                .body(UserWithInfoResponse.response("성공", (User) responses[1]));
    }

    @Operation(summary = "유저 정보 조회", description = "유저정보 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 토큰"),
    })
    @GetMapping("/users")
    public ResponseEntity<GetUserInfoResponse> userInfo(@ValidateToken String token) {
        return ResponseEntity.ok(userService.getUserInfo(token));
    }

    @Operation(summary = "유저 정보 수정", description = "유저정보 수정 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 토큰"),
    })
    @PatchMapping("/users")
    public ResponseEntity<UserWithInfoResponse> updateUser(@RequestBody UpdateUserRequest updateUserRequest, @ValidateToken String token) {
        return ResponseEntity.ok().body(userService.updateUser(token, updateUserRequest));
    }

    @Operation(summary = "유저 삭제", description = "유저삭제 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 토큰"),
    })
    @DeleteMapping("/users")
    public ResponseEntity<UserWithInfoResponse> deleteUser(@ValidateToken String token) {
        return ResponseEntity.ok().body(userService.deleteUser(token));
    }

    @Operation(summary = "유저 검색 (검색창에 리스트 띄움)", description = "ex) "+ "'hw' 검색 시"+ " 'hwan, hwanE, hwannnanana' ... 나옴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 토큰")
    })
    @GetMapping("/users/lists")
    public ResponseEntity<?> getUserList(@RequestBody GetUserListRequest userListRequest, @ValidateToken String token) {
        return ResponseEntity.ok().body(userService.getUserList(userListRequest, token));
    }

    @Operation(summary = "팔로잉 추가", description = "팔로잉, 팔로우 이거 하나로 해결")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 토큰"),
            @ApiResponse(responseCode = "404", description = "상대 유저의 아이디가 없습니다"),
            @ApiResponse(responseCode = "404", description = "이미 팔로잉 하셨습니다")
    })
    @PostMapping("/users/follow")
    public ResponseEntity<UserWithInfoResponse> addFollow(@RequestBody FollowRequest followRequest, @ValidateToken String token) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.addFollow(followRequest, token));
    }

    @Operation(summary = "팔로잉 삭제", description = "팔로잉 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 토큰"),
            @ApiResponse(responseCode = "400", description = "팔로잉 하지 않았습니다"),
            @ApiResponse(responseCode = "404", description = "상대 유저의 아이디가 없습니다")
    })
    @DeleteMapping("/users/follow")
    public ResponseEntity<UserWithInfoResponse> deleteFollow(@RequestBody FollowRequest followRequest, @ValidateToken String token) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteFollow(followRequest, token));
    }

    @Operation(summary = "팔로워 조회", description = "팔로워 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공 (빈 값 [] 도 조회됨)"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 토큰"),
    })
    @GetMapping("/users/follow/follower")
    public ResponseEntity<?> getFollower(@ValidateToken String token) {
        return ResponseEntity.ok().body(userService.getFollower(token));
    }

    @Operation(summary = "팔로잉 조회", description = "팔로잉 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공 (빈 값 [] 도 조회됨)"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 토큰"),
    })
    @GetMapping("/users/follow/following")
    public ResponseEntity<?> getFollowing(@ValidateToken String token) {
        return ResponseEntity.ok().body(userService.getFollowing(token));
    }

}
