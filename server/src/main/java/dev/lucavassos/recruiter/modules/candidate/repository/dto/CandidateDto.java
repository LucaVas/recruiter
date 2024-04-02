package dev.lucavassos.recruiter.modules.candidate.repository.dto;

import dev.lucavassos.recruiter.modules.candidacy.repository.dto.CandidacyDto;

import java.time.LocalDateTime;
import java.util.Set;

public record CandidateDto(
        Long id,
        String name,
        String phone,
        String email,
        Double totalExperience,
        String education,
        Double currentCtc,
        String pan,
        LocalDateTime createdAt
) {
}
