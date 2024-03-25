package dev.lucavassos.recruiter.modules.user.service;

import dev.lucavassos.recruiter.exception.DuplicateResourceException;
import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.exception.UnauthorizedException;
import dev.lucavassos.recruiter.modules.user.domain.*;
import dev.lucavassos.recruiter.modules.user.entities.PasswordResetToken;
import dev.lucavassos.recruiter.modules.user.generator.PasswordResetTokenGenerator;
import dev.lucavassos.recruiter.modules.user.repository.PasswordResetTokenRepository;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.jwt.JWTUtil;
import dev.lucavassos.recruiter.modules.user.repository.dto.UserDto;
import dev.lucavassos.recruiter.modules.user.repository.dto.UserDtoMapper;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PasswordResetTokenGenerator resetTokenGenerator;
    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private UserDtoMapper userDtoMapper;
    @Autowired
    private JWTUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        String email = request.email();
        String password = request.password();

        User user = repository.findOneByEmail(email)
                .orElseThrow(() ->
                        new UnauthorizedException("Invalid email or password. Please, try again.")
                );

        boolean isMatch = passwordEncoder.matches(password, user.getPassword());
        if (!isMatch) {
            throw new UnauthorizedException("Invalid email or password. Please, try again.");
        }

        UserDto userDto = userDtoMapper.apply(user);
        String token = jwtUtil.issueToken(userDto.id(), userDto.username());
        return new LoginResponse(token, userDto.username());
    }

    public SignupResponse signup(SignupRequest request) throws Exception {
        LOG.info("Initiated signup process in service...");
        String email = request.email();
        String mobile = request.mobile();

        if (repository.existsUserByEmail(email)) {
            throw new DuplicateResourceException(
                    "User with email %s already exists.".formatted(email)
            );
        }
        if (repository.existsUserByMobile(mobile)) {
            throw new DuplicateResourceException(
                    "User with mobile %s already exists.".formatted(mobile)
            );
        }

        User createdUser;
        try {
            createdUser = repository.save(
                    User.builder()
                            .username(request.username())
                            .email(request.email())
                            .password(passwordEncoder.encode(request.password()))
                            .mobile(request.mobile())
                            .city(request.city())
                            .country(request.country())
                            .role(request.role())
                            .build()
            );
        } catch (Exception e) {
            throw new Exception(e.getCause());
        }


        LOG.info("New user created: [{}]", createdUser);

        return new SignupResponse(createdUser.getId());
    }

    public void approveUser(Long id, UserApprovalRequest request) {
        User user = repository
                .findOneById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with id %d not found".formatted(id)
                        )
                );

        user.setApproved(request.approved());
        user.setComments(request.commments());
        user.setApprovedOn(LocalDateTime.now());
        // TODO: add approver

        repository.save(user);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = repository.findAll();
        return users
                .stream()
                .map(user -> userDtoMapper.apply(user))
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
        if (ChronoUnit.MINUTES.between(token.getCreatedAt(), now) > 15) {
            LOG.error("Token expired.");
            throw new BadRequestException("Invalid token. Please request a new one.");
        }

        User updatedUser = token.getUser();
        updatedUser.setPassword(passwordEncoder.encode(request.newPassword()));
        repository.save(updatedUser);
        tokenRepository.delete(token);
    }
}
