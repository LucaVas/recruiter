package dev.lucavassos.recruiter.auth.domain;

public record SignupRequest(
        String username,
        String email,
        String password,
        String mobile,
        String city,
        String country,
        String roleName,
        String comments
) {

}
