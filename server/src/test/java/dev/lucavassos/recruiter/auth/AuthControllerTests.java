package dev.lucavassos.recruiter.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lucavassos.recruiter.auth.domain.SignupRequest;
import dev.lucavassos.recruiter.auth.domain.SignupResponse;
import dev.lucavassos.recruiter.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AuthController.class})
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTests {

    private final String signupUrl = "/api/v1/auth/signup";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserDetailsService userDetailsService;


    @Test
    void register_Returns201_IfRequestIsValid() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("Name")
                .email("mail@mail.com")
                .password("Password123!")
                .phone("1234567890")
                .city("City")
                .country("Country")
                .roleName("RECRUITER")
                .build();
        SignupResponse response = new SignupResponse(1L);

        when(authService.register(request))
                .thenReturn(response);

        ResultActions result = mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        result
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)));

        verify(authService, times(1)).register(request);
    }

    @Test
    void register_Returns400_IfRequestHasNoName() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("")
                .email("mail@mail.com")
                .password("Password123!")
                .phone("1234567890")
                .city("City")
                .country("Country")
                .roleName("RECRUITER")
                .build();

        mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("One of more fields are invalid:\n- Invalid name. It must be between 3 and 50 characters long and can contain only letters, numbers, underscores, hyphens and dots\n")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("One of more fields are invalid")));

        verify(authService, never()).register(request);
    }

    @Test
    void register_Returns400_IfRequestHasInvalidName() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("<h1>test</h1>")
                .email("mail@mail.com")
                .password("Password123!")
                .phone("1234567890")
                .city("City")
                .country("Country")
                .roleName("RECRUITER")
                .build();

        mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("One of more fields are invalid:\n- Invalid name. It must be between 3 and 50 characters long and can contain only letters, numbers, underscores, hyphens and dots\n")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("One of more fields are invalid")));

        verify(authService, never()).register(request);
    }

    @Test
    void register_Returns400_IfRequestHasTooShortName() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("a")
                .email("mail@mail.com")
                .password("Password123!")
                .phone("1234567890")
                .city("City")
                .country("Country")
                .roleName("RECRUITER")
                .build();

        mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("One of more fields are invalid:\n- Invalid name. It must be between 3 and 50 characters long and can contain only letters, numbers, underscores, hyphens and dots\n")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("One of more fields are invalid")));

        verify(authService, never()).register(request);
    }

    @Test
    void register_Returns400_IfRequestHasTooLongName() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("testertestertestertestertestertestertestertestertestertestertestertestertestertestertester")
                .email("mail@mail.com")
                .password("Password123!")
                .phone("1234567890")
                .city("City")
                .country("Country")
                .roleName("RECRUITER")
                .build();

        mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("One of more fields are invalid:\n- Invalid name. It must be between 3 and 50 characters long and can contain only letters, numbers, underscores, hyphens and dots\n")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("One of more fields are invalid")));

        verify(authService, never()).register(request);
    }

    @Test
    void register_Returns400_IfRequestHasNoEmail() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("Name")
                .email("")
                .password("Password123!")
                .phone("1234567890")
                .city("City")
                .country("Country")
                .roleName("RECRUITER")
                .build();

        mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("One of more fields are invalid:\n- Email cannot be empty\n")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("One of more fields are invalid")));

        verify(authService, never()).register(request);
    }

    @Test
    void register_Returns400_IfRequestHasInvalidEmail() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("Name")
                .email("mail")
                .password("Password123!")
                .phone("1234567890")
                .city("City")
                .country("Country")
                .roleName("RECRUITER")
                .build();

        mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("One of more fields are invalid:\n- Invalid email\n")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("One of more fields are invalid")));

        verify(authService, never()).register(request);
    }

    @Test
    void register_Returns400_IfRequestHasNoPassword() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("Name")
                .email("mail@mail.com")
                .password("")
                .phone("1234567890")
                .city("City")
                .country("Country")
                .roleName("RECRUITER")
                .build();

        mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("One of more fields are invalid:\n- Password must be between 8 and 64 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character\n")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("One of more fields are invalid")));

        verify(authService, never()).register(request);
    }

    @Test
    void register_Returns400_IfRequestHasInvalidPassword() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("Name")
                .email("mail@mail.com")
                .password("password")
                .phone("1234567890")
                .city("City")
                .country("Country")
                .roleName("RECRUITER")
                .build();

        mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("One of more fields are invalid:\n- Password must be between 8 and 64 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character\n")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("One of more fields are invalid")));

        verify(authService, never()).register(request);
    }

    @Test
    void register_Returns400_IfRequestHasTooShortPassword() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("Name")
                .email("mail@mail.com")
                .password("Pa2!")
                .phone("1234567890")
                .city("City")
                .country("Country")
                .roleName("RECRUITER")
                .build();

        mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("One of more fields are invalid:\n- Password must be between 8 and 64 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character\n")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("One of more fields are invalid")));

        verify(authService, never()).register(request);
    }

    @Test
    void register_Returns400_IfRequestHasTooLongPassword() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("Name")
                .email("mail@mail.com")
                .password("Password123!Password123!Password123!Password123!Password123!Password123!Password123!Password123!Password123!Password123!Password123!Password123!Password123!Password123!Password123!")
                .phone("1234567890")
                .city("City")
                .country("Country")
                .roleName("RECRUITER")
                .build();

        mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("One of more fields are invalid:\n- Password must be between 8 and 64 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character\n")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("One of more fields are invalid")));

        verify(authService, never()).register(request);
    }

    @Test
    void register_Returns400_IfRequestHasNoPhone() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("Name")
                .email("mail@mail.com")
                .password("Password123!")
                .phone("")
                .city("City")
                .country("Country")
                .roleName("RECRUITER")
                .build();

        mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("One of more fields are invalid:\n- Mobile number must have 10 digits\n")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("One of more fields are invalid")));

        verify(authService, never()).register(request);
    }

    @Test
    void register_Returns400_IfRequestHasTooShortPhone() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("Name")
                .email("mail@mail.com")
                .password("Password123!")
                .phone("123")
                .city("City")
                .country("Country")
                .roleName("RECRUITER")
                .build();

        mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("One of more fields are invalid:\n- Mobile number must have 10 digits\n")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("One of more fields are invalid")));

        verify(authService, never()).register(request);
    }

    @Test
    void register_Returns400_IfRequestHasTooLongPhone() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("Name")
                .email("mail@mail.com")
                .password("Password123!")
                .phone("12345678901")
                .city("City")
                .country("Country")
                .roleName("RECRUITER")
                .build();

        mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("One of more fields are invalid:\n- Mobile number must have 10 digits\n")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("One of more fields are invalid")));

        verify(authService, never()).register(request);
    }

    @Test
    void register_Returns400_IfRequestHasInvalidPhone() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("Name")
                .email("mail@mail.com")
                .password("Password123!")
                .phone("+123456789")
                .city("City")
                .country("Country")
                .roleName("RECRUITER")
                .build();

        mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("One of more fields are invalid:\n- Mobile number must have 10 digits\n")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("One of more fields are invalid")));

        verify(authService, never()).register(request);
    }

    @Test
    void register_Returns400_IfRequestHasNoCity() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("Name")
                .email("mail@mail.com")
                .password("Password123!")
                .phone("1234567890")
                .city("")
                .country("Country")
                .roleName("RECRUITER")
                .build();

        mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("One of more fields are invalid:\n- City cannot be empty\n")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("One of more fields are invalid")));

        verify(authService, never()).register(request);
    }

    @Test
    void register_Returns400_IfRequestHasNoCountry() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("Name")
                .email("mail@mail.com")
                .password("Password123!")
                .phone("1234567890")
                .city("City")
                .country("")
                .roleName("RECRUITER")
                .build();

        mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("One of more fields are invalid:\n- Country cannot be empty\n")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("One of more fields are invalid")));

        verify(authService, never()).register(request);
    }

    @Test
    void register_Returns400_IfRequestHasNoRoleName() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("Name")
                .email("mail@mail.com")
                .password("Password123!")
                .phone("1234567890")
                .city("City")
                .country("Country")
                .roleName("")
                .build();

        mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("One of more fields are invalid:\n- Role name cannot be empty\n")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("One of more fields are invalid")));

        verify(authService, never()).register(request);
    }
}
