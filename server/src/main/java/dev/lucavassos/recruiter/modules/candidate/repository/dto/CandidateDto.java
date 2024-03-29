package dev.lucavassos.recruiter.modules.candidate.repository.dto;

import dev.lucavassos.recruiter.modules.candidate.domain.CandidateStatus;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.dto.RecruiterDto;

public record CandidateDto(
        Long id,
        String name,
        String phone,
        String email,
        Double totalExperience,
        Double relevantExperience,
        String education,
        Double currentCtc,
        Double expectedCtc,
        Double officialNoticePeriod,
        Double actualNoticePeriod,
        String reasonForQuickJoin,
        String remarks,
        RecruiterDto recruiter,
        String pan,
        String comments,
        CandidateStatus status
) {
}
