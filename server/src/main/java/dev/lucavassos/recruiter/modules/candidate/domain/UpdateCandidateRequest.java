package dev.lucavassos.recruiter.modules.candidate.domain;

import jakarta.annotation.Nullable;
import lombok.Builder;

@Builder
public record UpdateCandidateRequest(
        @Nullable String name,
        @Nullable String phone,
        @Nullable String email,
        @Nullable Double totalExperience,
        @Nullable String education,
        @Nullable Double currentCtc,
        @Nullable String pan
) {}
