package dev.lucavassos.recruiter.modules.candidate.domain;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record UpdateCandidateRequest(
        @Nullable
        String name,

        @NotBlank(message = "Phone cannot be empty")
        @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must have 10 digits.")
        String phone,

        @Nullable
        @Email(message = "Invalid email")
        String email,

        @Nullable
        @Min(value = 0, message = "Total experience must be a positive number")
        @Max(value = 50, message = "Total experience must be less than 51 years")
        Double totalExperience,

        @Nullable
        String education,

        @Nullable
        @Min(value = 0, message = "Current CTC must be a positive number")
        Double currentCtc,

        @NotBlank(message = "Pan cannot be empty")
        @Length(min = 10, max = 10, message = "Pan must have 10 characters")
        String pan
) {}
