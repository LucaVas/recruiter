package dev.lucavassos.recruiter.auth;

import dev.lucavassos.recruiter.exception.DuplicateResourceException;
import dev.lucavassos.recruiter.exception.ServerException;
import dev.lucavassos.recruiter.exception.UnauthorizedException;
import dev.lucavassos.recruiter.jwt.JwtTokenProvider;
import dev.lucavassos.recruiter.modules.user.entities.Role;
import dev.lucavassos.recruiter.modules.user.entities.RoleName;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.RoleRepository;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

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

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {

        LOG.info("Initiated signup process in service...");
        if (userRepository.existsUserByEmail(request.email())) {
            throw new DuplicateResourceException(
                    "User with email %s already exists.".formatted(request.email())
            );
        }
        if (userRepository.existsUserByMobile(request.mobile())) {
            throw new DuplicateResourceException(
                    "User with mobile %s already exists.".formatted(request.mobile())
            );
        }

        User user = User.builder()
                .name(request.name())
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .mobile(request.mobile())
                .city(request.city())
                .country(request.country())
                .build();

        Role userRole = roleRepository.findByName(RoleName.ROLE_RECRUITER)
                .orElseThrow(() -> new ServerException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        LOG.info("New user created: [{}]", user);
        return ResponseEntity
                .created(location)
                .body(new SignupResponse(user.getId()));
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

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = tokenProvider.generateToken(auth);

        LoginResponse res = new LoginResponse(jwt, "Bearer");
        LOG.info("Login successful. Response: {}", res);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, res.token())
                .body(res);
    }
}
