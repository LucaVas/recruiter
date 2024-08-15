package dev.lucavassos.recruiter.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lucavassos.recruiter.auth.domain.LoginRequest;
import dev.lucavassos.recruiter.auth.domain.SignupRequest;
import dev.lucavassos.recruiter.auth.domain.SignupResponse;
import dev.lucavassos.recruiter.exception.BadRequestException;
import dev.lucavassos.recruiter.exception.DatabaseException;
import dev.lucavassos.recruiter.exception.DuplicateResourceException;
import dev.lucavassos.recruiter.jwt.JwtService;
import dev.lucavassos.recruiter.modules.user.domain.RoleName;
import dev.lucavassos.recruiter.modules.user.entities.Role;
import dev.lucavassos.recruiter.modules.user.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
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
    private final String loginUrl = "/api/v1/auth/login";

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

    // Registration

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

    @Test
    void register_Returns400_IfEmailAlreadyExist() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("Name")
                .email("mail@mail.com")
                .password("Password123!")
                .phone("1234567890")
                .city("City")
                .country("Country")
                .roleName("RECRUITER")
                .build();

        when(authService.register(request))
                .thenThrow(new DuplicateResourceException("User with email mail@mail.com already exists."));

        ResultActions result = mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        result
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("User with email mail@mail.com already exists.")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("The resource already exists")));

        verify(authService, times(1)).register(request);
    }

    @Test
    void register_Returns400_IfPhoneAlreadyExist() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("Name")
                .email("mail@mail.com")
                .password("Password123!")
                .phone("1234567890")
                .city("City")
                .country("Country")
                .roleName("RECRUITER")
                .build();

        when(authService.register(request))
                .thenThrow(new DuplicateResourceException("User with phone 1234567890 already exists."));

        ResultActions result = mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        result
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("User with phone 1234567890 already exists.")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("The resource already exists")));

        verify(authService, times(1)).register(request);
    }

    @Test
    void register_Returns400_IfNameAlreadyExist() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("Name")
                .email("mail@mail.com")
                .password("Password123!")
                .phone("1234567890")
                .city("City")
                .country("Country")
                .roleName("RECRUITER")
                .build();

        when(authService.register(request))
                .thenThrow(new DuplicateResourceException("User with name Name already exists."));

        ResultActions result = mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        result
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("User with name Name already exists.")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("The resource already exists")));

        verify(authService, times(1)).register(request);
    }

    @Test
    void register_Returns400_IfRoleIsInvalid() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("Name")
                .email("mail@mail.com")
                .password("Password123!")
                .phone("1234567890")
                .city("City")
                .country("Country")
                .roleName("BAD_ROLE")
                .build();

        when(authService.register(request))
                .thenThrow(new BadRequestException("The user role provided is invalid."));

        ResultActions result = mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        result
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("The user role provided is invalid.")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("The request is invalid")));

        verify(authService, times(1)).register(request);
    }

    @Test
    void register_Returns500_IfDatabaseOperationFails() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .name("Name")
                .email("mail@mail.com")
                .password("Password123!")
                .phone("1234567890")
                .city("City")
                .country("Country")
                .roleName("BAD_ROLE")
                .build();

        when(authService.register(request))
                .thenThrow(new DatabaseException("Database exception"));

        ResultActions result = mockMvc
                .perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        result
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-500-internal-server-error")))
                .andExpect(jsonPath("$.title", is("Internal Server Error")))
                .andExpect(jsonPath("$.status", is(500)))
                .andExpect(jsonPath("$.detail", is("Database exception")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/signup")))
                .andExpect(jsonPath("$.description", is("Unknown internal server error.")));

        verify(authService, times(1)).register(request);
    }

    // Authentication

    @Test
    void authenticate_Returns200_IfRequestIsValid() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("mail@mail.com")
                .password("Password123!")
                .build();
        User user = User.builder()
                .id(1L)
                .email("mail@mail.com")
                .role(Role.builder().name(RoleName.ADMIN).build())
                .build();

        when(authService.authenticate(request))
                .thenReturn(user);

        ResultActions result = mockMvc
                .perform(post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id", is(1)))
                .andExpect(jsonPath("$.user.name", is(user.getUsername())))
                .andExpect(jsonPath("$.user.roleName", is(user.getRoleName().name())))
                .andExpect(jsonPath("$.tokenType", is("Bearer")));

        verify(authService, times(1)).authenticate(request);
    }

    @Test
    void authenticate_Returns400_IfRequestHasNoEmail() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("")
                .password("Password123!")
                .build();

        mockMvc
                .perform(post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("One of more fields are invalid:\n- Email cannot be empty\n")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/login")))
                .andExpect(jsonPath("$.description", is("One of more fields are invalid")));

        verify(authService, never()).authenticate(request);
    }

    @Test
    void authenticate_Returns400_IfRequestHasNoPassword() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("mail@mail.com")
                .password("")
                .build();

        mockMvc
                .perform(post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request")))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("One of more fields are invalid:\n- Password cannot be empty\n")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/login")))
                .andExpect(jsonPath("$.description", is("One of more fields are invalid")));

        verify(authService, never()).authenticate(request);
    }

    @Test
    void authenticate_Returns403_IfUserIsNotApproved() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("mail@mail.com")
                .password("Password123!")
                .build();

        when(authService.authenticate(request))
                .thenThrow(new AccessDeniedException("User access is not yet approved. Please, contact your administrator"));

        mockMvc
                .perform(post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-403-forbidden")))
                .andExpect(jsonPath("$.title", is("Forbidden")))
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.detail", is("User access is not yet approved. Please, contact your administrator")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/login")))
                .andExpect(jsonPath("$.description", is("You are not authorized to access this resource")));

        verify(authService, times(1)).authenticate(request);
    }

    @Test
    void authenticate_Returns403_IfCredentialsAreInvalid() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("mail@mail.com")
                .password("Password123!")
                .build();

        when(authService.authenticate(request))
                .thenThrow(new AccessDeniedException("Invalid credentials."));

        mockMvc
                .perform(post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.type", is("https://www.rfc-editor.org/rfc/rfc9110.html#name-403-forbidden")))
                .andExpect(jsonPath("$.title", is("Forbidden")))
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.detail", is("Invalid credentials.")))
                .andExpect(jsonPath("$.instance", is("/api/v1/auth/login")))
                .andExpect(jsonPath("$.description", is("You are not authorized to access this resource")));

        verify(authService, times(1)).authenticate(request);
    }

}
