package dev.lucavassos.recruiter.auth.controller;

import dev.lucavassos.recruiter.auth.*;
import dev.lucavassos.recruiter.auth.domain.AuthUserInfoDto;
import dev.lucavassos.recruiter.auth.service.AuthService;
import dev.lucavassos.recruiter.exception.UnauthorizedException;
import dev.lucavassos.recruiter.jwt.JwtTokenProvider;
import dev.lucavassos.recruiter.modules.user.repository.RoleRepository;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
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
    AuthService service;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        LOG.info("Received request for signup");
        SignupResponse res = service.signup(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest request) {
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

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = tokenProvider.generateToken(auth);

        LoginResponse res = new LoginResponse(userPrincipal.getId(), userPrincipal.getUsername(), jwt, "Bearer");
        LOG.info("Login successful. Response: {}", res);

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
