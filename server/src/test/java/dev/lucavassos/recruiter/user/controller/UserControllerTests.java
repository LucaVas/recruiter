package dev.lucavassos.recruiter.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lucavassos.recruiter.modules.user.jwt.JWTAuthenticationFilter;
import dev.lucavassos.recruiter.modules.user.controller.UserController;
import dev.lucavassos.recruiter.security.SecurityConfig;
import dev.lucavassos.recruiter.modules.user.domain.Role;
import dev.lucavassos.recruiter.modules.user.domain.SignupRequest;
import dev.lucavassos.recruiter.modules.user.domain.SignupResponse;
import dev.lucavassos.recruiter.modules.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UserController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@Import(SecurityConfig.class)
public class UserControllerTests {

    private static final String SIGNUP_URL = "/api/v1/signup";

    @MockBean private UserService service;
    @MockBean private JWTAuthenticationFilter filter;
    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void init_MockMvc() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    public void signup_creates_a_user () throws Exception {

        SignupRequest request = new SignupRequest(
                "username",
                "email",
                "password",
                "1234567890",
                "city",
                "country",
                Role.RECRUITER
        );
        SignupResponse res = new SignupResponse(1L);

        // Mocking the service behavior
        when(service.signup(request)).thenReturn(res);

        // Performing an HTTP POST request to signup
        this.mockMvc.perform(MockMvcRequestBuilders
                .post(SIGNUP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("username"));
    }
}
