package dev.lucavassos.recruiter.modules.candidate.domain;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
public record NewCandidateRequest(
        @NotBlank(message = "Name cannot be empty")
        String name,

        @NotBlank(message = "Phone cannot be empty")
        @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must have 10 digits.")
        String phone,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Invalid email")
        String email,

        @NotNull(message = "Total experience cannot be empty")
        @Min(value = 0, message = "Total experience must be a positive number")
        @Max(value = 50, message = "Total experience must be less than 51 years")
        Double totalExperience,

        @NotBlank(message = "Education cannot be empty")
        String education,

        @NotNull(message = "Current CTC cannot be empty")
        @Min(value = 0, message = "Current CTC must be a positive number")
        Double currentCtc,

        @NotBlank(message = "Pan cannot be empty")
        @Length(min = 10, max = 10, message = "Pan must have 10 characters")
        String pan
) {
}
