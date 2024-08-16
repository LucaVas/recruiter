package dev.lucavassos.recruiter.auth.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginRequest(
        @NotBlank(message = "Email cannot be empty")
        String email,
        @NotBlank(message = "Password cannot be empty")
        String password
) {}
