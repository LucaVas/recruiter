package dev.lucavassos.recruiter.modules.user.domain;

import dev.lucavassos.recruiter.modules.user.entities.Role;

public record SignupRequest(
        String username,
        String email,
        String password,
        String mobile,
        String city,
        String country,
        Role role
) {

}
