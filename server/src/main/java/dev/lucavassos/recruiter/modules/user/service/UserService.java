package dev.lucavassos.recruiter.modules.user.service;

import dev.lucavassos.recruiter.auth.UserPrincipal;
import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.modules.user.domain.*;
import dev.lucavassos.recruiter.modules.user.entities.PasswordResetToken;
import dev.lucavassos.recruiter.modules.user.generator.PasswordResetTokenGenerator;
import dev.lucavassos.recruiter.modules.user.repository.PasswordResetTokenRepository;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.dto.UserDto;
import dev.lucavassos.recruiter.modules.user.repository.dto.UserDtoMapper;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class UserService  {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenGenerator resetTokenGenerator;
    private final UserRepository repository;
    private final PasswordResetTokenRepository tokenRepository;
    private final UserDtoMapper userDtoMapper;

    public UserService(PasswordEncoder passwordEncoder, PasswordResetTokenGenerator resetTokenGenerator, UserRepository repository, PasswordResetTokenRepository tokenRepository, UserDtoMapper userDtoMapper) {
        this.passwordEncoder = passwordEncoder;
        this.resetTokenGenerator = resetTokenGenerator;
        this.repository = repository;
        this.tokenRepository = tokenRepository;
        this.userDtoMapper = userDtoMapper;
    }

    public void approveUser(UserApprovalRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        User user = repository
                .findOneById(request.userId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with id %d not found".formatted(request.userId())
                        )
                );

        User approver = repository
                .findOneById(userPrincipal.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Approver with id %d not found".formatted(userPrincipal.getId())
                        )
                );

        user.setApproved(request.approved());
        user.setComments(request.commments());
        user.setApprovedDTime(LocalDateTime.now());
        user.setApprover(approver);

        User approvedUser = repository.save(user);

        LOG.info("User approved: {}", approvedUser );
    }

    public List<UserDto> getAllUsers() {
        LOG.info("Request received for all users");
        List<User> users = repository.findAll();
        User user = getAuthUser();

        return users
                .stream()
                .filter(u -> !u.getId().equals(user.getId()))
                .map(userDtoMapper)
                .toList();

    }

    public void sendResetPasswordEmail(PasswordResetTokenRequest request) throws BadRequestException {
        User userByEmail = repository
                .findOneByEmail(request.email())
                .orElseThrow(() -> {
                    String message = String.format("User with email %s not found", request.email());
                        LOG.error(message);
                            return new ResourceNotFoundException(message);
                }
                );

        User userByUsername = repository
                .findOneByUsername(request.username())
                .orElseThrow(() -> {
                            String message = String.format("User with username %s not found", request.username());
                            LOG.error(message);
                            return new ResourceNotFoundException(message);
                        }
                );

        if (!userByEmail.getUsername().equals(userByUsername.getUsername())) {
            String message = String.format("Username of user with email %s (%s) does not match username provided %s",
                    userByEmail.getEmail(),
                    userByEmail.getUsername(),
                    request.username());
            LOG.error(message);
            throw new BadRequestException("Incorrect. Please try again.");
        }

        if (userByUsername.getPasswordResetToken() != null) {
            LOG.debug("Token for user [{}] already exists", userByUsername.getUsername());
            deleteTokenForUser(userByUsername);
        }

        PasswordResetToken token = PasswordResetToken.builder()
                .tokenString(resetTokenGenerator.generateNewToken())
                .user(userByUsername)
                .build();

        tokenRepository.save(token);

        // TODO: send email with url
    }

    public void deleteTokenForUser(User user) {
        PasswordResetToken token = user.getPasswordResetToken();
        if (token != null) {
            tokenRepository.delete(token);
            LOG.debug("Token after delete exists: {}", tokenRepository.existsById(token.getId()));
            user.setPasswordResetToken(null);
        }
    }

    public void resetPassword(String tokenString, PasswordResetRequest request) throws BadRequestException {
        PasswordResetToken token =
                tokenRepository
                        .findOneByTokenString(tokenString)
                        .orElseThrow(() -> {
                                    LOG.error("Token was not found in the database.");
                                    return new BadRequestException("Invalid token. Please request a new one.");
                                }
                        );

        LocalDateTime now = LocalDateTime.now();
        if (ChronoUnit.MINUTES.between(token.getCreatedDTime(), now) > 15) {
            LOG.error("Token expired.");
            throw new BadRequestException("Invalid token. Please request a new one.");
        }

        User updatedUser = token.getUser();
        updatedUser.setPassword(passwordEncoder.encode(request.newPassword()));
        repository.save(updatedUser);
        tokenRepository.delete(token);
    }

    private User getAuthUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return repository.findOneById(userPrincipal.getId()).orElseThrow(
                () -> {
                    LOG.error("User with id {} not found", userPrincipal.getId());
                    return new ResourceNotFoundException("User not found");
                }
        );
    }
}
