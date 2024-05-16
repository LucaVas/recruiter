package dev.lucavassos.recruiter.auth.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public record LoginRequest(
        @NotBlank(message = "Email cannot be empty")
        String email,
        @NotBlank(message = "Password cannot be empty")
        String password
) {}
