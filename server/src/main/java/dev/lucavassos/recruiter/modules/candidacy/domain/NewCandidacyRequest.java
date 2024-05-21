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

        @NotBlank(message = "Candidate pan is required")
        @Length(min = 10, max = 10, message = "Candidate pan must have 10 characters")
        String candidatePan,

        @NotNull(message = "Candidate relevant experience is required")
        @Min(value = 0, message = "Candidate relevant experience must be a positive number")
        @Max(value = 50, message = "Candidate relevant experience must be less than 51 years")
        Double relevantExperience,

        @NotNull(message = "Candidate expected CTC is required")
        @Min(value = 0, message = "Candidate expected CTC must be a positive number")
        Double expectedCtc,

        @NotNull(message = "Candidate official notice period is required")
        @Min(value = 0, message = "Candidate official notice period must be a positive number")
        Double officialNoticePeriod,

        @NotNull(message = "Candidate actual notice period is required")
        @Min(value = 0, message = "Candidate actual notice period must be a positive number")
        Double actualNoticePeriod,

        @Nullable
        CandidacyStatus status,

        String reasonForQuickJoin,

        String recruiterComment,

        @Nullable MultipartFile resume
) {
}
