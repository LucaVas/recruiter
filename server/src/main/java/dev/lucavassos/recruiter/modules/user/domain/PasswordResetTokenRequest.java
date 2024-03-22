package dev.lucavassos.recruiter.modules.user.domain;

public record PasswordResetTokenRequest(
        String email,
        String username
) {
}
