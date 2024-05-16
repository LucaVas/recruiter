package dev.lucavassos.recruiter.auth.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateProfileRequest(

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Invalid email")
        String email,

        @NotBlank(message = "Phone cannot be empty")
        @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must have 10 digits.")
        String phone,

        @NotBlank(message = "City cannot be empty")
        String city
) {

}
