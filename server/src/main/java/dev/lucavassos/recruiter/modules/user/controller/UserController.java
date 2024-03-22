package dev.lucavassos.recruiter.modules.user.controller;

import dev.lucavassos.recruiter.modules.user.domain.*;
import dev.lucavassos.recruiter.modules.user.service.UserService;
import dev.lucavassos.recruiter.modules.user.repository.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = service.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, response.token())
                .body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) throws Exception {
        LOG.info("POST on /signup");
        return new ResponseEntity<>(service.signup(request), HttpStatus.OK);
    }

//    @PutMapping("/users/{userId}")
//    public void updateUser(
//            @PathVariable("userId") Long userId,
//            @RequestBody UserUpdateRequest request) {
//        service.updateUser(userId, request);
//    }

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
}
