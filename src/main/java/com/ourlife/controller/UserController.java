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
    public ResponseEntity<Void> signup(@RequestBody SignupRequest signupRequest) {
        userService.signup(signupRequest.toEntity(passwordEncoder));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/users")
    public ResponseEntity<GetUserInfoResponse> userInfo(@ValidateToken String token) {
        return ResponseEntity.ok(userService.getUserInfo(token));
    }

    @PatchMapping("/users")
    public ResponseEntity<Void> updateUser(@RequestBody UpdateUserRequest updateUserRequest, @ValidateToken String token) {
        userService.updateUser(token, updateUserRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUser(@ValidateToken String token) {
        userService.deleteUser(token);
        return ResponseEntity.ok().build();
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
    public ResponseEntity<Void> addFollow(@RequestBody FollowRequest followRequest, @ValidateToken String token) {
        userService.addFollow(followRequest, token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/users/follow")
    public ResponseEntity<Void> deleteFollow(@RequestBody FollowRequest followRequest, @ValidateToken String token) {
        userService.deleteFollow(followRequest, token);
        return ResponseEntity.status(HttpStatus.OK).build();
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
