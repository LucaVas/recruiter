package dev.lucavassos.recruiter.modules.candidate.domain;

import lombok.*;

@Builder
public record NewCandidateRequest(
        String name,
        String phone,
        String email,
        double totalExperience,
        String education,
        double currentCtc,
        String pan
) {}
