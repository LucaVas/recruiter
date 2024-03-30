package dev.lucavassos.recruiter.modules.candidacy.repository.dto;

import dev.lucavassos.recruiter.modules.candidate.repository.dto.CandidateDto;
import dev.lucavassos.recruiter.modules.job.repository.dto.JobDto;
import dev.lucavassos.recruiter.modules.user.repository.dto.RecruiterDto;

import java.time.LocalDateTime;

public record CandidacyDto(
        Long id,
        JobDto job,
        RecruiterDto recruiter,
        CandidateDto candidate,
        double relevantExperience,
        double expectedCtc,
        double officialNoticePeriod,
        double actualNoticePeriod,
        String reasonForQuickJoin,
        String remarks,
        String comments,
        LocalDateTime cvRatePaymentDate,
        LocalDateTime closureBonusPaymentDate,
        LocalDateTime createdAt
) {
}
