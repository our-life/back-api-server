package com.ourlife.controller;

import com.ourlife.dto.user.GetUserInfoResponse;
import com.ourlife.dto.user.SigninRequest;
import com.ourlife.dto.user.SignupRequest;
import com.ourlife.dto.user.UpdateUserRequest;
import com.ourlife.service.UserService;
import com.ourlife.utils.Impl.JwtTokenUtils;
import com.ourlife.utils.PasswordEncoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
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

    @PostMapping("/users/signin")
    public ResponseEntity<Void> signin(@RequestBody SigninRequest signinRequest) {
        return ResponseEntity.ok().
                header("Authorization", "Bearer " + userService.signin(signinRequest)).build();
    }

    @GetMapping("/users")
    public ResponseEntity<GetUserInfoResponse> userInfo(HttpServletRequest req) {
        String token = jwtTokenUtils.resolveToken(req);
        return ResponseEntity.ok(userService.getUserInfo(token));
    }

    @PatchMapping("/users")
    public ResponseEntity<Void> updateUser(@RequestBody UpdateUserRequest updateUserRequest, HttpServletRequest req) {
        String token = jwtTokenUtils.resolveToken(req);
        userService.updateUser(token, updateUserRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUser(HttpServletRequest req) {
        String token = jwtTokenUtils.resolveToken(req);
        userService.deleteUser(token);
        return ResponseEntity.ok().build();
    }
}
