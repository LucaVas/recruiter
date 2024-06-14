package dev.lucavassos.recruiter.modules.user;

import dev.lucavassos.recruiter.auth.domain.UpdateProfileRequest;
import dev.lucavassos.recruiter.exception.DuplicateResourceException;
import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.exception.ServerException;
import dev.lucavassos.recruiter.modules.user.domain.PasswordForgotRequest;
import dev.lucavassos.recruiter.modules.user.domain.PasswordResetRequest;
import dev.lucavassos.recruiter.modules.user.domain.UserApprovalRequest;
import dev.lucavassos.recruiter.modules.user.entities.PasswordResetToken;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.PasswordResetTokenRepository;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
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
public class UserService {

    @Value("${password.reset.token.expirationInSeconds}")
    private Integer expirationInSeconds;

    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenGenerator resetTokenGenerator;
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final UserDtoMapper userDtoMapper;
    private final EmailService emailService;

    public void approveUser(UserApprovalRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User userPrincipal = (User) authentication.getPrincipal();

        User user = userRepository
                .findOneById(request.userId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with id %d not found".formatted(request.userId())
                        )
                );

        User approver = userRepository
                .findOneById(userPrincipal.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Approver with id %d not found".formatted(userPrincipal.getId())
                        )
                );


        user.setApproved(request.approved());
        user.setApprovedAt(DateTimeUtils.getCurrentDTime());
        user.setApprover(approver);
        user.setComment(request.comment());

        log.info("User to approve: {} on {}", user.getComment(), user.getApprovedAt());

        User approvedUser = userRepository.save(user);

        log.info("User approved: {}", approvedUser);
    }

    public List<UserDto> getAllUsers() {
        log.info("Request received for all users");
        List<User> users = userRepository.findAll();
        User user = getAuthUser();

        return users
                .stream()
                .filter(u -> !u.getId().equals(user.getId()))
                .map(userDtoMapper)
                .toList();

    }

    public void sendResetPasswordEmail(PasswordForgotRequest request) throws BadRequestException, MessagingException {
        User userByEmail = userRepository
                .findOneByEmail(request.email())
                .orElseThrow(() -> {
                            String message = String.format("User with email %s not found", request.email());
                            log.error(message);
                            return new ResourceNotFoundException(message);
                        }
                );

        User userByName = userRepository
                .findOneByName(request.name())
                .orElseThrow(() -> {
                            String message = String.format("User with name %s not found", request.name());
                            log.error(message);
                            return new ResourceNotFoundException(message);
                        }
                );

        if (!userByEmail.getName().equals(userByName.getName())) {
            String message = String.format("Username of user with email %s (%s) does not match name provided %s",
                    userByEmail.getUsername(),
                    userByEmail.getName(),
                    request.name());
            log.error(message);
            throw new BadRequestException("Incorrect. Please try again.");
        }

        if (userByName.getPasswordResetToken() != null) {
            log.debug("Token for user [{}] already exists", userByName.getName());
            deleteTokenForUser(userByName);
        }

        PasswordResetToken token = PasswordResetToken.builder()
                .tokenString(resetTokenGenerator.generateNewToken())
                .user(userByName)
                .build();

        tokenRepository.save(token);

        log.info("Sending email to [{}]", userByName.getUsername());
        emailService.sendEmail(userByName.getUsername(),
                userByName.getName(),
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
        if (ChronoUnit.SECONDS.between(token.getCreatedAt(), now) > expirationInSeconds) {
            log.error("Token expired.");
            throw new BadRequestException("Invalid token. Please request a new one.");
        }

        User updatedUser = token.getUser();
        updatedUser.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(updatedUser);
        tokenRepository.delete(token);
    }

    public UserDto getAuthUserProfile() {
        User user = getAuthUser();
        return userRepository.findOneById(user.getId())
                .map(userDtoMapper)
                .orElseThrow(() -> new ServerException("Auth user not found."));
    }

    public void updateAuthUserProfile(UpdateProfileRequest request) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User userPrincipal = (User) authentication.getPrincipal();

        User user = userRepository.findOneById(userPrincipal.getId())
                .orElseThrow(() -> new ServerException("Auth user not found."));

        if (request.email().equals(user.getUsername())
                && request.phone().equals(user.getPhone())
                && request.city().equals(user.getCity())) {
            return;
        }

        if (!request.email().equals(user.getUsername())) {

            if (userRepository.existsUserByEmail(request.email())) throw new DuplicateResourceException(
                    "User with email %s already exists.".formatted(request.email())
            );

            user.setEmail(request.email());
        }
        if (!request.phone().equals(user.getPhone())) {
            if (userRepository.existsUserByPhone(request.phone())) throw new DuplicateResourceException(
                    "User with phone %s already exists.".formatted(request.phone())
            );

            user.setPhone(request.phone());
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

    private User getAuthUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User userPrincipal = (User) authentication.getPrincipal();
        return userRepository.findOneById(userPrincipal.getId()).orElseThrow(
                () -> {
                    log.error("User with id {} not found", userPrincipal.getId());
                    return new ResourceNotFoundException("User not found");
                }
        );
    }
}
