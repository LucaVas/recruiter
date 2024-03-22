package dev.lucavassos.recruiter.modules.candidate.repository.dto;

import dev.lucavassos.recruiter.modules.candidate.domain.CandidateStatus;
import dev.lucavassos.recruiter.modules.user.entities.User;

public record CandidateDto(
        Long id,
        String name,
        String phone,
        String email,
        Double totalExperience,
        Double relevantExperience,
        String education,
        Double currentCtC,
        Double expectedCtC,
        Double officialNoticePeriod,
        Double actualNoticePeriod,
        String reasonForQuickJoin,
        String remarks,
        User recruiter,
        String pan,
        String comments,
        CandidateStatus status
) {
}
