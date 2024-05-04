package dev.lucavassos.recruiter.auth.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateProfileRequest(

        @NotBlank(message = "Username or email cannot be empty")
        @Email(message = "Invalid email")
        String email,

        @NotBlank(message = "Mobile cannot be empty")
        @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must have 10 digits.")
        String mobile,

        @NotBlank(message = "City cannot be empty")
        String city
) {

}
