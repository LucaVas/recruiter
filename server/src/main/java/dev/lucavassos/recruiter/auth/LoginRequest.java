package dev.lucavassos.recruiter.auth;

public record AuthenticationRequest(
        String email,
        String password
) {

}
