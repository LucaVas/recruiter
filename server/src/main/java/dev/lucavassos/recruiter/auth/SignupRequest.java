package dev.lucavassos.recruiter.auth;

public record SignupRequest(
        String name,
        String username,
        String email,
        String password,
        String mobile,
        String city,
        String country) {

}
