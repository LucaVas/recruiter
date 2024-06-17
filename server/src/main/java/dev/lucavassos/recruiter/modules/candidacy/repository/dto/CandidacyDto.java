package dev.lucavassos.recruiter.modules.candidacy.repository.dto;

import dev.lucavassos.recruiter.modules.candidacy.domain.CandidacyStatus;
import dev.lucavassos.recruiter.modules.candidate.repository.dto.CandidateDto;
import dev.lucavassos.recruiter.modules.job.repository.dto.JobDTO;
import dev.lucavassos.recruiter.modules.user.repository.dto.RecruiterDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public final class CandidacyDto {
    private final JobDTO job;
    private final RecruiterDto recruiter;
    private final CandidateDto candidate;
    private final Double relevantExperience;
    private final Double expectedCtc;
    private final Double officialNoticePeriod;
    private final Double actualNoticePeriod;
    private final String reasonForQuickJoin;
    private final CandidacyStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
