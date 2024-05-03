package dev.lucavassos.recruiter.modules.candidacy.repository.dto;

import dev.lucavassos.recruiter.modules.candidacy.domain.CandidacyStatus;
import dev.lucavassos.recruiter.modules.candidate.repository.dto.CandidateDto;
import dev.lucavassos.recruiter.modules.job.repository.dto.JobDto;
import dev.lucavassos.recruiter.modules.user.repository.dto.RecruiterDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CandidacyDto(
        JobDto job,
        RecruiterDto recruiter,
        CandidateDto candidate,
        Double relevantExperience,
        Double expectedCtc,
        Double officialNoticePeriod,
        Double actualNoticePeriod,
        String reasonForQuickJoin,
        CandidacyStatus status,
        LocalDateTime createdDTime,
        LocalDateTime modifiedDTime
) {
}
