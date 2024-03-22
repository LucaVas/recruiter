package dev.lucavassos.recruiter.modules.user.domain;

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
