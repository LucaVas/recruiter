package dev.lucavassos.recruiter.modules.user;

import dev.lucavassos.recruiter.auth.domain.UpdateProfileRequest;
import dev.lucavassos.recruiter.modules.user.domain.*;
import dev.lucavassos.recruiter.modules.user.repository.dto.UserDto;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1")
public class UserController {

    private final UserService service;

    @PostMapping("/users/approvals")
    public ResponseEntity<?> approveUser(
            @Valid @RequestBody UserApprovalRequest request) {
        log.info("Received request to approve users: {}", request);
        service.approveUser(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("Received request for users.");
        return ResponseEntity.ok(service.getAllUsers());
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> sendResetPasswordToken(
            @RequestBody PasswordForgotRequest request) throws BadRequestException, MessagingException {
        service.sendResetPasswordEmail(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/resetPassword/{token}")
    public ResponseEntity<?> verify(
            @PathVariable("token") String token,
            @Valid @RequestBody PasswordResetRequest request) throws BadRequestException {
        service.resetPassword(token, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/users/profile")
    public ResponseEntity<UserDto> getAuthUserProfile() {
        log.info("Received request for auth user profile.");
        return ResponseEntity.ok(service.getAuthUserProfile());
    }

    @PutMapping("/users/profile/update")
    public ResponseEntity<?> updateAuthUserProfile(
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        log.info("Received request to update auth user profile.");
        service.updateAuthUserProfile(request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
