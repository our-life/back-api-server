package com.ourlife.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ourlife.Fixture;
import com.ourlife.dto.user.GetUserInfoResponse;
import com.ourlife.dto.user.SignupRequest;
import com.ourlife.exception.AccountNotFoundException;
import com.ourlife.exception.DuplicatedEmailException;
import com.ourlife.service.UserService;
import com.ourlife.utils.Impl.JwtTokenUtils;
import com.ourlife.utils.PasswordEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    JwtTokenUtils jwtTokenUtils;

    public String makeUriValidationDuplication(String email) {
        return "/users/" + email + "/validation";
    }

    public String makeSignupUri() {
        return "/users";
    }

    public String makeGetUserInfoUri() { return "/users"; }

    @Test
    @DisplayName("사용가능한 이메일인 경우")
    void validationDuplicationEmailOk() throws Exception {
        String email = Fixture.user().getEmail();
        String uri = makeUriValidationDuplication(email);

        when(userService.validateDuplicationEmail(email)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("사용불가능한 이메일인 경우")
    void validationDuplicationEmailConflict() throws Exception {
        String email = Fixture.user().getEmail();
        String uri = makeUriValidationDuplication(email);

        when(userService.validateDuplicationEmail(email)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("회원가입 성공")
    void signupSuccess() throws Exception {
        String uri = makeSignupUri();
        SignupRequest request = Fixture.signupRequest();
        String requestString = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .content(requestString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원가입 실패 중복된 이메일")
    void signupFail_duplicated_email() throws Exception {
        String uri = makeSignupUri();
        SignupRequest request = Fixture.signupRequest();
        String requestString = objectMapper.writeValueAsString(request);

        doThrow(DuplicatedEmailException.class)
                .when(userService).signup(any());

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .content(requestString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("회원 정보 조회")
    void userInfo() throws Exception {
        String uri = makeGetUserInfoUri();

        when(jwtTokenUtils.resolveToken(any())).thenReturn("123");
        when(userService.getUserInfo(anyString()))
                .thenReturn(GetUserInfoResponse.from(Fixture.user(1L)));

        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .header("Authorization", "Bearer " + anyString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nickname").exists())
                .andExpect(jsonPath("$.birth").exists())
                .andExpect(jsonPath("$.introduce").exists())
                .andExpect(jsonPath("$.profileImgUrl").exists());
    }

    @Test
    @DisplayName("회원 정보 조회 실패 토큰 invalidate")
    void userInfo_fail_invalid_token() throws Exception {
        String uri = makeGetUserInfoUri();

        when(jwtTokenUtils.resolveToken(any())).thenReturn("123");
        doThrow(IllegalStateException.class)
                .when(userService).getUserInfo(anyString());

        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .header("Authorization", "Bearer " + anyString()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원 정보 조회 실패 토큰 user_not_found")
    void userInfo_fail_user_not_found() throws Exception {
        String uri = makeGetUserInfoUri();

        when(jwtTokenUtils.resolveToken(any())).thenReturn("123");
        doThrow(AccountNotFoundException.class)
                .when(userService).getUserInfo(anyString());

        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .header("Authorization", "Bearer " + anyString()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
