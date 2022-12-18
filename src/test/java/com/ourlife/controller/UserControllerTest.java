package com.ourlife.controller;

import com.ourlife.Fixture;
import com.ourlife.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    public String makeUriValidationDuplication(String email) {
        return "/users/" + email + "/validation";
    }

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
}
