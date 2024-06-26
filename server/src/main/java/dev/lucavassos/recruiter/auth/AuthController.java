package dev.lucavassos.recruiter.auth;

import dev.lucavassos.recruiter.auth.domain.*;
import dev.lucavassos.recruiter.jwt.JwtService;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthService service;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest request) {
        log.info("Received request for signup");
        SignupResponse res = service.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest request) {
        log.info("New authentication request received: {}", request);

        User authenticatedUser = service.authenticate(request);

        Map<String, Object> userClaim = new HashMap<>();
        userClaim.put("user", new AuthUserInfoDto(authenticatedUser.getId(), authenticatedUser.getName(), authenticatedUser.getRole().getName()));
        String jwtToken = jwtService.generateToken(userClaim, authenticatedUser);

        LoginResponse loginResponse = new LoginResponse(
                new AuthUserInfoDto(authenticatedUser.getId(), authenticatedUser.getUsername(), authenticatedUser.getRole().getName()),
                jwtToken,
                "Bearer");

        log.info("Authentication successful");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .body(loginResponse);
    }
}
