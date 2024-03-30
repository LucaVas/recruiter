package dev.lucavassos.recruiter.modules.candidate.domain;

import dev.lucavassos.recruiter.modules.candidate.repository.dto.NewCandidateDto;

public record NewCandidateResponse(
    Long id,
    NewCandidateDto candidate
) {

}
