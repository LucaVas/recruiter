package dev.lucavassos.recruiter.modules.user.domain;

public record LoginRequest(
        String email,
        String password
) {

}
