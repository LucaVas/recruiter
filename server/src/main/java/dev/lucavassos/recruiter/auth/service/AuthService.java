package dev.lucavassos.recruiter.auth.service;

import dev.lucavassos.recruiter.auth.domain.*;
import dev.lucavassos.recruiter.auth.UserPrincipal;
import dev.lucavassos.recruiter.exception.DuplicateResourceException;
import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.exception.ServerException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MonitoringProcessor monitoringProcessor;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        log.info("Initiated signup process in service...{}", request);
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
        if (userRepository.existsUserByUsername(request.username())) {
            throw new DuplicateResourceException(
                    "User with username %s already exists.".formatted(request.username())
            );
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .mobile(request.mobile())
                .city(request.city())
                .country(request.country())
                .build();

        log.info("Role: {}", request.roleName());
        Role userRole = roleRepository.findByName(RoleName.valueOf(request.roleName()))
                .orElseThrow(() -> new ServerException("The user role provided is invalid."));

        user.setRoles(Collections.singleton(userRole));

        User userSaved = userRepository.save(user);

        log.info("New user created: [{}]", userSaved);
        monitoringProcessor.incrementUsersCounter();

        return new SignupResponse(user.getId());
    }

    public AuthUserInfoDto getAuthUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return new AuthUserInfoDto(
                userPrincipal.getId(),
                userPrincipal.getUsername(),
                userPrincipal.getRoleName());
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
                && request.mobile().equals(user.getMobile())
                && request.city().equals(user.getCity())) {
            return;
        }

        if (!request.email().equals(user.getEmail())) {

            if (userRepository.existsUserByEmail(request.email())) throw new DuplicateResourceException(
                    "User with email %s already exists.".formatted(request.email())
            );

            user.setEmail(request.email());
        }
        if (!request.mobile().equals(user.getMobile())) {
            if (userRepository.existsUserByMobile(request.mobile())) throw new DuplicateResourceException(
                    "User with mobile %s already exists.".formatted(request.mobile())
            );

            user.setMobile(request.mobile());
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
