package dev.lucavassos.recruiter.modules.candidate.domain;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
public record NewCandidateRequest(
        @NotBlank(message = "Candidate name is required")
        String name,

        @NotBlank(message = "Candidate phone is required")
        @Pattern(regexp = "^[0-9]{10}$", message = "Candidate phone number must have 10 digits.")
        String phone,

        @NotBlank(message = "Candidate email is required")
        @Email(message = "Invalid candidate email")
        String email,

        @NotNull(message = "Candidate total experience is required")
        @Min(value = 0, message = "Candidate total experience must be a positive number")
        @Max(value = 50, message = "Candidate total experience must be less than 51 years")
        Double totalExperience,

        @NotBlank(message = "Candidate education is required")
        String education,

        @NotNull(message = "Candidate current CTC is required")
        @Min(value = 0, message = "Candidate current CTC must be a positive number")
        Double currentCtc,

        @NotBlank(message = "Candidate pan is required")
        @Length(min = 10, max = 10, message = "Candidate pan must have 10 characters")
        String pan
) {
}
