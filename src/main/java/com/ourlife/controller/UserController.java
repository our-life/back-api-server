package com.ourlife.controller;

import com.ourlife.dto.user.SigninRequest;
import com.ourlife.dto.user.SignupRequest;
import com.ourlife.service.UserService;
import com.ourlife.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

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
    public ResponseEntity<String> signin(@RequestBody SigninRequest signinRequest){
        return ResponseEntity.ok().body(userService.signin(signinRequest));
    }

}
