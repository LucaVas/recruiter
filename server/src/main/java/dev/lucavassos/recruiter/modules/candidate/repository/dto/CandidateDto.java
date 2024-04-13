package dev.lucavassos.recruiter.modules.candidate.repository.dto;


import java.time.LocalDateTime;

public record CandidateDto(
        String name,
        String phone,
        String email,
        Double totalExperience,
        String education,
        Double currentCtc,
        String pan,
        LocalDateTime createdAt
){}
