package dev.lucavassos.recruiter.modules.candidacy.domain;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record NewCandidacyCommentRequest(

        @NotBlank(message = "Candidacy comment text is required")
        @Length(min = 1, max = 500, message = "Candidacy comment text must be between 1 and 500 characters")
        String text
) {
}
