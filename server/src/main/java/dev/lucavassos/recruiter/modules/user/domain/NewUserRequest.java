package dev.lucavassos.recruiter.modules.user.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record NewUserRequest(

        @NotBlank(message = "Name cannot be empty")
        @Pattern(regexp = "^[a-zA-Z0-9_\\-.]+$", message = "Invalid name. It can contain only letters, numbers, underscores, hyphens and dots")
        String name,

        @NotBlank(message = "Username or email cannot be empty")
        @Email(message = "Invalid email")
        String email,

        @NotBlank(message = "Mobile cannot be empty")
        @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must have 10 digits.")
        String phone,

        @NotBlank(message = "City cannot be empty")
        String city,

        @NotBlank(message = "Country cannot be empty")
        String country,

        @NotNull(message = "Role name cannot be empty")
        String roleName
) {}
