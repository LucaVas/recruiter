package dev.lucavassos.recruiter.modules.candidacy.domain;


import jakarta.validation.constraints.NotNull;

public record ChangeCandidacyStatusRequest(
        @NotNull(message = "Candidacy status is required")
        CandidacyStatus status
) {}
