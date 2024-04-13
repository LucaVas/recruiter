package dev.lucavassos.recruiter.modules.candidacy.domain;

import dev.lucavassos.recruiter.modules.candidacy.repository.dto.CandidacyDto;

public record CandidacyResponse(
        Long id,
        CandidacyDto candidacy
) {
}