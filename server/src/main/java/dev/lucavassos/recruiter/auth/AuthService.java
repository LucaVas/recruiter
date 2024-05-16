package dev.lucavassos.recruiter.auth;

import dev.lucavassos.recruiter.auth.domain.*;
import dev.lucavassos.recruiter.exception.*;
import dev.lucavassos.recruiter.modules.user.entities.Role;
import dev.lucavassos.recruiter.modules.user.domain.RoleName;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.RoleRepository;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import dev.lucavassos.recruiter.modules.user.repository.dto.UserDto;
import dev.lucavassos.recruiter.modules.user.repository.dto.UserDtoMapper;
import dev.lucavassos.recruiter.monitoring.MonitoringProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials."));
    }

    @Transactional
    private void validateUserExistence(SignupRequest request) {
        if (userRepository.existsUserByEmail(request.email())) {
            throw new DuplicateResourceException(
                    "User with email %s already exists.".formatted(request.email())
            );
        }
        if (userRepository.existsUserByMobile(request.phone())) {
            throw new DuplicateResourceException(
                    "User with phone %s already exists.".formatted(request.phone())
            );
        }
        if (userRepository.existsUserByUsername(request.name())) {
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

    public UserDto getAuthUserProfile() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return userRepository.findOneById(userPrincipal.getId())
                .map(userDtoMapper)
                .orElseThrow(() -> new ServerException("Auth user not found."));
    }

    public void updateAuthUserProfile(UpdateProfileRequest request) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        User user = userRepository.findOneById(userPrincipal.getId())
                .orElseThrow(() -> new ServerException("Auth user not found."));

        if (request.email().equals(user.getEmail())
                && request.mobile().equals(user.getPhone())
                && request.city().equals(user.getCity())) {
            return;
        }

        if (!request.email().equals(user.getEmail())) {

            if (userRepository.existsUserByEmail(request.email())) throw new DuplicateResourceException(
                    "User with email %s already exists.".formatted(request.email())
            );

            user.setEmail(request.email());
        }
        if (!request.mobile().equals(user.getPhone())) {
            if (userRepository.existsUserByMobile(request.mobile())) throw new DuplicateResourceException(
                    "User with phone %s already exists.".formatted(request.mobile())
            );

            user.setPhone(request.mobile());
        }

        // set new city
        if (!request.city().equals(user.getCity())) {
            user.setCity(request.city());
        }

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new ServerException("Error updating user profile.");
        }
    }
}
