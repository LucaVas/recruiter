package dev.lucavassos.recruiter.auth;

import dev.lucavassos.recruiter.auth.domain.*;
import dev.lucavassos.recruiter.jwt.JwtService;
import dev.lucavassos.recruiter.modules.user.entities.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtService jwtService;
    private final AuthService service;
    private static final String JWT_TOKEN_TYPE = "Bearer";
    private static final String JWT_USER_CLAIM_KEY = "User";

    @PostMapping("/signup")
    public ResponseEntity<?> register(
            @Valid @RequestBody SignupRequest request
    ) {
        log.debug("Received request for registration: {}", request);
        SignupResponse res = service.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(
            @Valid @RequestBody LoginRequest request
    ) {
        log.debug("Request received for authentication: {}", request);

        User authenticatedUser = service.authenticate(request);
        String jwtToken = getToken(authenticatedUser);

        log.debug("Authentication successful");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .body(buildLoginResponse(authenticatedUser, jwtToken));
    }

    private String getToken(User user) {
        Map<String, Object> userClaim = new HashMap<>();
        userClaim.put(JWT_USER_CLAIM_KEY, new AuthUserInfoDto(user.getId(), user.getName(), user.getRole().getName()));
        return jwtService.generateToken(userClaim, user);
    }

    private LoginResponse buildLoginResponse(User user, String token) {
        return new LoginResponse(
                new AuthUserInfoDto(user.getId(), user.getUsername(), user.getRole().getName()),
                token,
                JWT_TOKEN_TYPE);
    }
}
