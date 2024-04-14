package dev.lucavassos.recruiter.auth.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
        @NotBlank(message = "Username or email cannot be empty")
        private String usernameOrEmail;

        @NotBlank(message = "Password cannot be empty")
        String password;
}
