package dev.lucavassos.recruiter.modules.candidate.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
public record NewCandidateRequest(
        @NotBlank(message = "Name cannot be empty")
        String name,
        @NotBlank(message = "Phone cannot be empty")
        String phone,
        @NotBlank(message = "Email cannot be empty") @Email(message = "Invalid email")
        String email,
        Double totalExperience,
        @NotBlank(message = "Education cannot be empty")
        String education,
        Double currentCtc,
        @NotBlank(message = "Pan cannot be empty") @Length(min = 10, max = 10)
        String pan
) {
}
