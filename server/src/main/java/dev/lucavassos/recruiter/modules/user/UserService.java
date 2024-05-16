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
import dev.lucavassos.recruiter.service.email.EmailService;
import dev.lucavassos.recruiter.utils.DateTimeUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService  {

    @Value("${password.reset.token.expirationInSeconds}")
    private Integer expirationInSeconds;

    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenGenerator resetTokenGenerator;
    private final UserRepository repository;
    private final PasswordResetTokenRepository tokenRepository;
    private final UserDtoMapper userDtoMapper;
    private final EmailService emailService;

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

    public void sendResetPasswordEmail(PasswordForgotRequest request) throws BadRequestException, MessagingException {
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

        if (!userByEmail.getName().equals(userByUsername.getName())) {
            String message = String.format("Username of user with email %s (%s) does not match username provided %s",
                    userByEmail.getEmail(),
                    userByEmail.getName(),
                    request.username());
            log.error(message);
            throw new BadRequestException("Incorrect. Please try again.");
        }

        if (userByUsername.getPasswordResetToken() != null) {
            log.debug("Token for user [{}] already exists", userByUsername.getName());
            deleteTokenForUser(userByUsername);
        }

        PasswordResetToken token = PasswordResetToken.builder()
                .tokenString(resetTokenGenerator.generateNewToken())
                .user(userByUsername)
                .build();

        tokenRepository.save(token);

        log.info("Sending email to [{}]", userByUsername.getEmail());
        emailService.sendEmail(userByUsername.getEmail(),
                userByUsername.getName(),
                token.getTokenString(),
                expirationInSeconds / 60);
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
        if (ChronoUnit.SECONDS.between(token.getCreatedDTime(), now) > expirationInSeconds) {
            log.error("Token expired.");
            throw new BadRequestException("Invalid token. Please request a new one.");
        }

        User updatedUser = token.getUser();
        updatedUser.setPassword(passwordEncoder.encode(request.password()));
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
