package dev.lucavassos.recruiter.auth.service;

import dev.lucavassos.recruiter.auth.domain.SignupRequest;
import dev.lucavassos.recruiter.auth.domain.SignupResponse;
import dev.lucavassos.recruiter.auth.UserPrincipal;
import dev.lucavassos.recruiter.auth.domain.AuthUserInfoDto;
import dev.lucavassos.recruiter.exception.DuplicateResourceException;
import dev.lucavassos.recruiter.exception.ServerException;
import dev.lucavassos.recruiter.modules.user.entities.Role;
import dev.lucavassos.recruiter.modules.user.domain.RoleName;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.RoleRepository;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import dev.lucavassos.recruiter.monitoring.MonitoringProcessor;
import lombok.RequiredArgsConstructor;
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
public class AuthService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MonitoringProcessor monitoringProcessor;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        LOG.info("Initiated signup process in service...{}", request);
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

        LOG.info("Role: {}", request.roleName());
        Role userRole = roleRepository.findByName(RoleName.valueOf(request.roleName()))
                .orElseThrow(() -> new ServerException("The user role provided is invalid."));

        user.setRoles(Collections.singleton(userRole));

        User userSaved = userRepository.save(user);

        LOG.info("New user created: [{}]", userSaved);
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
}
