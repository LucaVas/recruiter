package dev.lucavassos.recruiter.modules.user;

import dev.lucavassos.recruiter.auth.domain.UpdateProfileRequest;
import dev.lucavassos.recruiter.exception.*;
import dev.lucavassos.recruiter.modules.HistoryEventType;
import dev.lucavassos.recruiter.modules.user.domain.*;
import dev.lucavassos.recruiter.modules.user.entities.PasswordResetToken;
import dev.lucavassos.recruiter.modules.user.entities.Role;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.entities.UserHistory;
import dev.lucavassos.recruiter.modules.user.repository.PasswordResetTokenRepository;
import dev.lucavassos.recruiter.modules.user.repository.RoleRepository;
import dev.lucavassos.recruiter.modules.user.repository.UserHistoryRepository;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import dev.lucavassos.recruiter.modules.user.repository.dto.UserDto;
import dev.lucavassos.recruiter.modules.user.repository.dto.UserDtoMapper;
import dev.lucavassos.recruiter.service.email.NewUserPasswordEmailService;
import dev.lucavassos.recruiter.service.email.PasswordResetEmailService;
import dev.lucavassos.recruiter.utils.DateTimeUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static dev.lucavassos.recruiter.utils.RandomUtils.generateRandomPassword;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenGenerator resetTokenGenerator;
    private final UserRepository userRepository;
    private final UserHistoryRepository historyRepository;
    private final RoleRepository roleRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final UserDtoMapper userDtoMapper;
    private final PasswordResetEmailService passwordResetEmailService;
    private final NewUserPasswordEmailService newUserPasswordEmailService;

    @Value("${password.reset.token.expirationInSeconds}")
    private Integer expirationInSeconds;

    @Transactional
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
        saveUserHistoryEvent(approver, user, HistoryEventType.UPDATED);

        log.info("User approved: {}", approvedUser);
    }

    @Transactional
    public PaginatedUsersResponse getAllUsers(Integer page, Integer pageSize, String sort) {
        User user = getAuthUser();

        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by("name").ascending());
        Page<User> usersPage = userRepository.findAllButMe(user.getId(), pageRequest);

        PaginatedUsersResponse res = PaginatedUsersResponse.builder()
                .elements(usersPage.getContent().stream().map(userDtoMapper).toList())
                .page(usersPage.getNumber())
                .totalPages(usersPage.getTotalPages())
                .totalElements(usersPage.getTotalElements())
                .build();
        log.info("Users retrieved: {}", res);
        return res;
    }

    @Transactional
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
        passwordResetEmailService.sendPasswordResetEmail(userByName.getUsername(),
                userByName.getName(),
                token.getTokenString());
    }

    @Transactional
    public void deleteTokenForUser(User user) {
        PasswordResetToken token = user.getPasswordResetToken();
        if (token != null) {
            tokenRepository.delete(token);
            log.debug("Token after delete exists: {}", tokenRepository.existsById(token.getId()));
            user.setPasswordResetToken(null);
        }
    }

    @Transactional
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

    @Transactional
    public UserDto getAuthUserProfile() {
        User user = getAuthUser();
        return userRepository.findOneById(user.getId())
                .map(userDtoMapper)
                .orElseThrow(() -> new ServerException("Auth user not found."));
    }

    @Transactional
    public void updateAuthUserProfile(UpdateProfileRequest request) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User userPrincipal = (User) authentication.getPrincipal();

        User user = userRepository.findOneById(userPrincipal.getId())
                .orElseThrow(() -> new ServerException("Auth user not found."));

        if (request.email().equals(user.getUsername())
                && request.phone().equals(user.getPhone())
                && request.city().equals(user.getCity())) {
            throw new BadRequestException("No changes were made.");
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
            saveUserHistoryEvent(user, user, HistoryEventType.UPDATED);
        } catch (Exception e) {
            throw new ServerException("Error updating user profile.");
        }
    }

    @Transactional
    public void createUser(NewUserRequest request) throws MessagingException {
        validateUserExistence(request);

        User authUser = getAuthUser();
        String randomPassword = generateRandomPassword();

        User newUser = buildUser(request, authUser, randomPassword);
        User created = createUser(newUser);
        log.info("New user created: [{}]", created);

        newUserPasswordEmailService.sendEmailWithPassword(
                created.getUsername(),
                created.getName(),
                getAuthUser().getUsername(),
                randomPassword
        );
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = getAuthUser();
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new BadRequestException("Old password is incorrect.");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    private void validateUserExistence(NewUserRequest request) {
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

    private User createUser(User user) {
        try {
            User savedUser = userRepository.save(user);
            saveUserHistoryEvent(savedUser, savedUser, HistoryEventType.CREATED);
            return savedUser;
        } catch (Exception e) {
            log.error("Error creating user: [{}]", user, e);
            throw new DatabaseException("Error while creating user.");
        }
    }

    private User buildUser(NewUserRequest request, User creator, String randomPassword) {
        Role userRole = roleRepository.findByName(RoleName.valueOf(request.roleName()))
                .orElseThrow(() -> new BadRequestException("The user role provided is invalid."));

        User newUser = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(randomPassword))
                .phone(request.phone())
                .city(request.city())
                .country(request.country())
                .role(userRole)
                .build();

        if (creator.isAdmin()) {
            newUser.setApproved(true);
            newUser.setApprovedAt(LocalDateTime.now());
            newUser.setApprover(creator);
        }

        return newUser;
    }

    private void saveUserHistoryEvent(User modifiedBy, User user, HistoryEventType eventType) {
        try {
            UserHistory event = UserHistory.builder()
                    .name(user.getName())
                    .email(user.getUsername())
                    .phone(user.getPhone())
                    .city(user.getCity())
                    .country(user.getCountry())
                    .approved(user.isApproved())
                    .eventType(eventType)
                    .user(user)
                    .modifiedBy(modifiedBy)
                    .build();
            historyRepository.save(event);
        } catch (Exception e) {
            log.error("Database error while saving question history event: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
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
