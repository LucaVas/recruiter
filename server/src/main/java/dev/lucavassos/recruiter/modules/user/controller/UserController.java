package dev.lucavassos.recruiter.modules.user.controller;

import dev.lucavassos.recruiter.modules.user.domain.*;
import dev.lucavassos.recruiter.modules.user.service.UserService;
import dev.lucavassos.recruiter.modules.user.repository.dto.UserDto;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService service;

    @PostMapping("/users/{userId}/approve")
    public void approveUser(
            @PathVariable("userId") Long userId,
            @RequestBody UserApprovalRequest request) {
        service.approveUser(userId, request);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        LOG.info("Received request for users.");
        return new ResponseEntity<>(service.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/resetEmail/tokens")
    public void sendResetToken(
            @RequestBody PasswordResetTokenRequest request) throws BadRequestException {
        service.sendResetPasswordEmail(request);
    }

    @PostMapping("/resetEmail/{token}")
    public void verify(
            @PathVariable("token") String token,
            @RequestBody PasswordResetRequest request) throws BadRequestException {
        service.resetPassword(token, request);
    }
}
