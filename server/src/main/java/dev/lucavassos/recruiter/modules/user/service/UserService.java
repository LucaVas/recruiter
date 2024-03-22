package dev.lucavassos.recruiter.modules.user.service;

import dev.lucavassos.recruiter.exception.DuplicateResourceException;
import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.exception.UnauthorizedException;
import dev.lucavassos.recruiter.modules.user.controller.UserController;
import dev.lucavassos.recruiter.modules.user.domain.*;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.jwt.JWTUtil;
import dev.lucavassos.recruiter.modules.user.repository.dto.UserDto;
import dev.lucavassos.recruiter.modules.user.repository.dto.UserDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository repository;
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
                    "User with email [%s] already exists.".formatted(email)
            );
        }
        if (repository.existsUserByMobile(mobile)) {
            throw new DuplicateResourceException(
                    "User with mobile [%s] already exists.".formatted(mobile)
            );
        }

        User createdUser;
        try {
            createdUser = repository.save(
                    new User(
                            request.username(),
                            request.email(),
                            passwordEncoder.encode(request.password()),
                            request.mobile(),
                            request.city(),
                            request.country(),
                            request.role()
                    ));
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
                                "User with id [%d] not found".formatted(id)
                        )
                );

        user.setApproved(request.approved());
        user.setComments(request.commments());
        user.setApprovedOn(Date.valueOf(LocalDate.now()));
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
}
