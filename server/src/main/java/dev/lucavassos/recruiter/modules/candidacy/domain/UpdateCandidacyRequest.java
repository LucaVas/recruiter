package dev.lucavassos.recruiter.modules.candidacy.domain;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateCandidacyRequest(

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
        String remarks
) {
}
