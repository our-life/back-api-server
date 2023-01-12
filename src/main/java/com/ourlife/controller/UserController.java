package com.ourlife.controller;

import com.ourlife.argumentResolver.ValidateToken;
import com.ourlife.dto.user.*;
import com.ourlife.service.UserService;
import com.ourlife.utils.Impl.JwtTokenUtils;
import com.ourlife.utils.PasswordEncoder;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenUtils jwtTokenUtils;

    @GetMapping("/users/{email}/validation")
    public ResponseEntity<Void> validationDuplicationEmail(@PathVariable("email") String email) {
        boolean result = userService.validateDuplicationEmail(email);
        return result ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponse> signup(@RequestBody SignupRequest signupRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.signup(signupRequest.toEntity(passwordEncoder)));
    }

    @GetMapping("/users")
    public ResponseEntity<GetUserInfoResponse> userInfo(@ValidateToken String token) {
        return ResponseEntity.ok(userService.getUserInfo(token));
    }

    @PatchMapping("/users")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UpdateUserRequest updateUserRequest, @ValidateToken String token) {
        return ResponseEntity.ok().body(userService.updateUser(token, updateUserRequest));
    }

    @DeleteMapping("/users")
    public ResponseEntity<UserResponse> deleteUser(@ValidateToken String token) {
        return ResponseEntity.ok().body(userService.deleteUser(token));
    }

    @GetMapping("/users/lists")
    public ResponseEntity<?> getUserList(@RequestBody GetUserListRequest userListRequest, @ValidateToken String token) {
        return ResponseEntity.ok().body(userService.getUserList(userListRequest, token));
    }

    @PostMapping("/users/signin")
    public ResponseEntity<Void> signin(@RequestBody SigninRequest signinRequest) {
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + userService.signin(signinRequest)).build();
    }

    @PostMapping("/users/follow")
    public ResponseEntity<UserResponse> addFollow(@RequestBody FollowRequest followRequest, @ValidateToken String token) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.addFollow(followRequest, token));
    }

    @DeleteMapping("/users/follow")
    public ResponseEntity<UserResponse> deleteFollow(@RequestBody FollowRequest followRequest, @ValidateToken String token) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteFollow(followRequest, token));
    }


    @GetMapping("/users/follow/follower")
    public ResponseEntity<?> getFollower(@ValidateToken String token) {
        return ResponseEntity.ok().body(userService.getFollower(token));
    }

    @GetMapping("/users/follow/following")
    public ResponseEntity<Object> getFollowing(@ValidateToken String token) {
        return ResponseEntity.ok().body(userService.getFollowing(token));
    }
}
