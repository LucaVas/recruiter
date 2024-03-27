package dev.lucavassos.recruiter.auth;

import dev.lucavassos.recruiter.modules.user.entities.Role;

public record SignupRequest(
        String name,
        String username,
        String email,
        String password,
        String mobile,
        String city,
        String country) {

}
