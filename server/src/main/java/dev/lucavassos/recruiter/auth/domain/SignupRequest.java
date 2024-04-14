package dev.lucavassos.recruiter.auth.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SignupRequest(

        @NotBlank(message = "Username cannot be empty")
        @Pattern(regexp = "^[a-zA-Z0-9_\\-.]+$", message = "Invalid username. It can contain only letters, numbers, underscores, hyphens and dots")
        String username,

        @NotBlank(message = "Username or email cannot be empty")
        @Email(message = "Invalid email")
        String email,

        @NotBlank(message = "Password cannot be empty")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()-+=])[A-Za-z\\d!@#$%^&*()-+=]{8,64}$",
                message = "Password must be between 8 and 64 characters long "
                + "and contain at least one uppercase letter, one lowercase letter, "
                + "one digit, and one special character")
        String password,

        @NotBlank(message = "Mobile cannot be empty")
        @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must have 10 digits.")
        String mobile,

        @NotBlank(message = "City cannot be empty")
        String city,

        @NotBlank(message = "Country cannot be empty")
        String country,

        @NotNull(message = "Role name cannot be empty")
        String roleName,
        String comments
) {

}
