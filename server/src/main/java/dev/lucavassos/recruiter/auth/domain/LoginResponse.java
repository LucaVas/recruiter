package dev.lucavassos.recruiter.auth.domain;

public record LoginResponse(
        AuthUserInfoDto user,
        String token,
        String tokenType
) {
}
