package dev.lucavassos.recruiter.modules.candidate.repository.dto;


import java.time.LocalDateTime;

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
){}
