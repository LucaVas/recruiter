package dev.lucavassos.recruiter.auth;

import dev.lucavassos.recruiter.auth.domain.*;
import dev.lucavassos.recruiter.auth.service.AuthService;
import dev.lucavassos.recruiter.exception.UnauthorizedException;
import dev.lucavassos.recruiter.jwt.JwtService;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import dev.lucavassos.recruiter.modules.user.repository.dto.UserDto;
import dev.lucavassos.recruiter.monitoring.MonitoringProcessor;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping(value = "api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final MonitoringProcessor monitoringProcessor;
    private final AuthService service;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest request) {
        Instant start = Instant.now();
        log.info("Received request for signup");

        SignupResponse res = service.register(request);
        monitoringProcessor.observeGetSignupTime(start);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest request) {
        Instant start = Instant.now();
        log.info("New login request received: {}", request);

        Authentication auth;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsernameOrEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException ae) {
            throw new UnauthorizedException("Invalid credentials");
        }

        log.info("Authenticated user: {}", auth);
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        userRepository.findApprovedUserById(userPrincipal.getId())
                .orElseThrow(() -> {
                    log.error("User with ID [{}] is not approved.", userPrincipal.getId());
                    return new UnauthorizedException("User access is not approved yet.");
                });


        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = jwtService.generateToken(auth);

        LoginResponse res = new LoginResponse(
                new AuthUserInfoDto(userPrincipal.getId(), userPrincipal.getUsername(), userPrincipal.getRoleName()),
                jwt,
                "Bearer");

        log.info("Login successful. Response: {}", res);
        monitoringProcessor.observeGetLoginTime(start);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, res.token())
                .body(res);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getAuthUserProfile() {
        log.info("Received request for auth user profile.");
        return ResponseEntity.ok(service.getAuthUserProfile());
    }

    @PutMapping("/profile/update")
    public ResponseEntity<?> updateAuthUserProfile(
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        log.info("Received request to update auth user profile.");
        service.updateAuthUserProfile(request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
