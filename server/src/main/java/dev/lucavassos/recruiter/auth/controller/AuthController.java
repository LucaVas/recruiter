package dev.lucavassos.recruiter.auth.controller;

import dev.lucavassos.recruiter.auth.*;
import dev.lucavassos.recruiter.auth.domain.*;
import dev.lucavassos.recruiter.auth.service.AuthService;
import dev.lucavassos.recruiter.exception.UnauthorizedException;
import dev.lucavassos.recruiter.jwt.JwtTokenProvider;
import dev.lucavassos.recruiter.modules.user.repository.RoleRepository;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import dev.lucavassos.recruiter.monitoring.MonitoringProcessor;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping(value = "api/v1/auth")
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenProvider tokenProvider;
    @Autowired
    MonitoringProcessor monitoringProcessor;

    @Autowired
    AuthService service;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        Instant start = Instant.now();
        LOG.info("Received request for signup");

        SignupResponse res = service.signup(request);
        monitoringProcessor.observeGetSignupTime(start);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest request) {
        Instant start = Instant.now();
        LOG.info("New login request received: {}", request);

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

        LOG.info("Authenticated user: {}", auth);
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        userRepository.findApprovedUserById(userPrincipal.getId())
                .orElseThrow(() -> {
                    LOG.error("User with ID [{}] is not approved.", userPrincipal.getId());
                    return new UnauthorizedException("User access is not approved yet.");
                });


        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = tokenProvider.generateToken(auth);

        LoginResponse res = new LoginResponse(
                new AuthUserInfoDto(userPrincipal.getId(), userPrincipal.getUsername(), userPrincipal.getRoleName()),
                jwt,
                "Bearer");

        LOG.info("Login successful. Response: {}", res);
        monitoringProcessor.observeGetLoginTime(start);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, res.token())
                .body(res);
    }

    @GetMapping("/me")
    public ResponseEntity<AuthUserInfoDto> getAuthUser() {
        LOG.info("Received request for auth user.");
        return new ResponseEntity<>(service.getAuthUser(), HttpStatus.OK);
    }
}
