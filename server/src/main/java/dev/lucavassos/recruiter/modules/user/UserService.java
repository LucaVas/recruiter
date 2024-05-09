package dev.lucavassos.recruiter.modules.user;

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
import dev.lucavassos.recruiter.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
public class UserService  {

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
        user.setApprovedDTime(DateTimeUtils.getCurrentDTime());
        user.setApprover(approver);
        user.setComment(request.comment());

        log.info("User to approve: {} on {}", user.getComment(), user.getApprovedDTime());

        User approvedUser = repository.save(user);

        log.info("User approved: {}", approvedUser );
    }

    public List<UserDto> getAllUsers() {
        log.info("Request received for all users");
        List<User> users = repository.findAll();
        User user = getAuthUser();

        return users
                .stream()
                .filter(u -> !u.getId().equals(user.getId()))
                .map(userDtoMapper)
                .toList();

    }

    public void sendResetPasswordEmail(PasswordForgotRequest request) throws BadRequestException {
        User userByEmail = repository
                .findOneByEmail(request.email())
                .orElseThrow(() -> {
                    String message = String.format("User with email %s not found", request.email());
                        log.error(message);
                            return new ResourceNotFoundException(message);
                }
                );

        User userByUsername = repository
                .findOneByUsername(request.username())
                .orElseThrow(() -> {
                            String message = String.format("User with username %s not found", request.username());
                            log.error(message);
                            return new ResourceNotFoundException(message);
                        }
                );

        if (!userByEmail.getUsername().equals(userByUsername.getUsername())) {
            String message = String.format("Username of user with email %s (%s) does not match username provided %s",
                    userByEmail.getEmail(),
                    userByEmail.getUsername(),
                    request.username());
            log.error(message);
            throw new BadRequestException("Incorrect. Please try again.");
        }

        if (userByUsername.getPasswordResetToken() != null) {
            log.debug("Token for user [{}] already exists", userByUsername.getUsername());
            deleteTokenForUser(userByUsername);
        }

        PasswordResetToken token = PasswordResetToken.builder()
                .tokenString(resetTokenGenerator.generateNewToken())
                .user(userByUsername)
                .build();

        tokenRepository.save(token);

        // TODO: send email with url
        log.info("Token generated for user [{}]", userByUsername.getUsername());
    }

    public void deleteTokenForUser(User user) {
        PasswordResetToken token = user.getPasswordResetToken();
        if (token != null) {
            tokenRepository.delete(token);
            log.debug("Token after delete exists: {}", tokenRepository.existsById(token.getId()));
            user.setPasswordResetToken(null);
        }
    }

    public void resetPassword(String tokenString, PasswordResetRequest request) throws BadRequestException {
        PasswordResetToken token =
                tokenRepository
                        .findOneByTokenString(tokenString)
                        .orElseThrow(() -> {
                                    log.error("Token was not found in the database.");
                                    return new BadRequestException("Invalid token. Please request a new one.");
                                }
                        );

        LocalDateTime now = LocalDateTime.now();
        if (ChronoUnit.MINUTES.between(token.getCreatedDTime(), now) > 15) {
            log.error("Token expired.");
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
                    log.error("User with id {} not found", userPrincipal.getId());
                    return new ResourceNotFoundException("User not found");
                }
        );
    }
}
