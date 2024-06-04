package dev.lucavassos.recruiter.modules.candidate.repository.dto;

import dev.lucavassos.recruiter.modules.candidate.domain.CandidateStatus;

import java.time.LocalDateTime;

public record CandidateDto(
        String pan,
        String name,
        String phone,
        String email,
        Double totalExperience,
        String education,
        Double currentCtc,
        CandidateStatus status,
        LocalDateTime createdDTime,
        LocalDateTime modifiedDTime
) {}
