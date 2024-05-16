package dev.lucavassos.recruiter.auth;

import dev.lucavassos.recruiter.auth.domain.*;
import dev.lucavassos.recruiter.exception.*;
import dev.lucavassos.recruiter.modules.user.entities.Role;
import dev.lucavassos.recruiter.modules.user.domain.RoleName;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.RoleRepository;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import dev.lucavassos.recruiter.modules.user.repository.dto.UserDtoMapper;
import dev.lucavassos.recruiter.monitoring.MonitoringProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDtoMapper userDtoMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MonitoringProcessor monitoringProcessor;

    public SignupResponse register(SignupRequest request) {
        validateUserExistence(request);
        User createdUser = createUser(buildUser(request));
        log.info("New user created: [{}]", createdUser);
        monitoringProcessor.incrementUsersCounter();
        return new SignupResponse(createdUser.getId());
    }

    @Transactional
    public User authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        return userRepository
                .findOneByEmail(request.email())
                .orElseThrow(() -> new AccessDeniedException("Invalid credentials."));
    }

    @Transactional
    private void validateUserExistence(SignupRequest request) {
        if (userRepository.existsUserByEmail(request.email())) {
            throw new DuplicateResourceException(
                    "User with email %s already exists.".formatted(request.email())
            );
        }
        if (userRepository.existsUserByPhone(request.phone())) {
            throw new DuplicateResourceException(
                    "User with phone %s already exists.".formatted(request.phone())
            );
        }
        if (userRepository.existsUserByName(request.name())) {
            throw new DuplicateResourceException(
                    "User with name %s already exists.".formatted(request.name())
            );
        }
    }

    @Transactional
    private User createUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            log.error("Error creating user: [{}]", user, e);
            throw new DatabaseException("Error while creating user.");
        }
    }

    @Transactional
    private User buildUser(SignupRequest request) {
        Role userRole = roleRepository.findByName(RoleName.valueOf(request.roleName()))
                .orElseThrow(() -> new BadRequestException("The user role provided is invalid."));

        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phone(request.phone())
                .city(request.city())
                .country(request.country())
                .role(userRole)
                .build();
    }
}
