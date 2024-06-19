package dev.lucavassos.recruiter.modules.candidacy.domain;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record NewCandidacyRequest(

        @NotNull
        Long jobId,

        @NotBlank(message = "Candidate is required")
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

        @Nullable List<MultipartFile> files
) {
}
