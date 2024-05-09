package dev.lucavassos.recruiter.modules.user.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordResetRequest(

        @NotBlank(message = "New password cannot be empty")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()-+=])[A-Za-z\\d!@#$%^&*()-+=]{8,64}$",
                message = "New password must be between 8 and 64 characters long "
                        + "and contain at least one uppercase letter, one lowercase letter, "
                        + "one digit, and one special character")
        String password
) {
}
