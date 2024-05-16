package dev.lucavassos.recruiter.modules.user.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordForgotRequest(

        @NotBlank(message = "Email cannot be empty")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email")
        String email,

        @NotBlank(message = "Name cannot be empty")
        @Pattern(regexp = "^[a-zA-Z0-9_\\-.]+$", message = "Invalid name. It can contain only letters, numbers, underscores, hyphens and dots")
        String name
) {
}
