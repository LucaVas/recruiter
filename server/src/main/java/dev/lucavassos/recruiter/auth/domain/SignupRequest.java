package dev.lucavassos.recruiter.auth.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record SignupRequest(

        @Pattern(regexp = "^[a-zA-Z0-9_\\-.]{3,50}$",
                message = "Invalid name. It must be between 3 and 50 characters long " +
                        "and can contain only letters, numbers, underscores, hyphens and dots")
        String name,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Invalid email")
        String email,

        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()-+=])[A-Za-z\\d!@#$%^&*()-+=]{8,64}$",
                message = "Password must be between 8 and 64 characters long "
                        + "and contain at least one uppercase letter, one lowercase letter, "
                        + "one digit, and one special character"
        )
        String password,

        @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must have 10 digits")
        String phone,

        @NotBlank(message = "City cannot be empty")
        String city,

        @NotBlank(message = "Country cannot be empty")
        String country,

        @NotBlank(message = "Role name cannot be empty")
        String roleName,

        String comments
) {}
