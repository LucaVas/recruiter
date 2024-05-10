package dev.lucavassos.recruiter.modules.candidacy.domain;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

public record NewCandidacyRequest(

        @NotNull Long jobId,

        @NotBlank(message = "Pan cannot be empty")
        @Length(min = 10, max = 10, message = "Pan must have 10 characters")
        String candidatePan,

        @NotNull(message = "Relevant experience cannot be empty")
        @Min(value = 0, message = "Relevant experience must be a positive number")
        @Max(value = 50, message = "Relevant experience must be less than 51 years")
        Double relevantExperience,

        @NotNull(message = "Expected CTC cannot be empty")
        @Min(value = 0, message = "Expected CTC must be a positive number")
        Double expectedCtc,

        @NotNull(message = "Official notice period cannot be empty")
        @Min(value = 0, message = "Official notice period must be a positive number")
        Double officialNoticePeriod,

        @NotNull(message = "Actual notice period cannot be empty")
        @Min(value = 0, message = "Actual notice period must be a positive number")
        Double actualNoticePeriod,

        @Nullable
        CandidacyStatus status,

        String reasonForQuickJoin,
        String recruiterComment,
        MultipartFile resume
) {
}
