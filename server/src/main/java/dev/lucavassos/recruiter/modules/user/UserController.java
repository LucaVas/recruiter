package dev.lucavassos.recruiter.modules.user;

import dev.lucavassos.recruiter.auth.domain.UpdateProfileRequest;
import dev.lucavassos.recruiter.modules.user.domain.*;
import dev.lucavassos.recruiter.modules.user.repository.dto.UserDto;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/users")
public class UserController {

    private final UserService service;

    @PostMapping("/approvals")
    public ResponseEntity<?> approveUser(
            @Valid @RequestBody UserApprovalRequest request) {
        log.info("Received request to approve users: {}", request);
        service.approveUser(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<PaginatedUsersResponse> getAllUsers(
            @RequestParam(name = "page", required = false, defaultValue = "0") final Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "5") final Integer pageSize,
            @RequestParam(name = "sort", required = false, defaultValue = "asc") final String sort
    ) {
        log.info("Received request for users: page {}, pageSize {}, sort {}", pageNumber, pageSize, sort);
        PaginatedUsersResponse response = service.getAllUsers(pageNumber, pageSize, sort);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createUser(
            @Valid @RequestBody NewUserRequest request) throws MessagingException {
        log.info("Received request to create user: {}", request);
        service.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/password")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {
        log.info("Received request to change password.");
        service.changePassword(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> sendResetPasswordToken(
            @RequestBody PasswordForgotRequest request) throws MessagingException {
        service.sendResetPasswordEmail(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/resetPassword/{token}")
    public ResponseEntity<?> verify(
            @PathVariable("token") String token,
            @Valid @RequestBody PasswordResetRequest request) {
        service.resetPassword(token, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getAuthUserProfile() {
        log.info("Received request for auth user profile.");
        return ResponseEntity.ok(service.getAuthUserProfile());
    }

    @PutMapping("/profile/update")
    public ResponseEntity<?> updateAuthUserProfile(
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        log.info("Received request to update auth user profile.");
        service.updateAuthUserProfile(request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
