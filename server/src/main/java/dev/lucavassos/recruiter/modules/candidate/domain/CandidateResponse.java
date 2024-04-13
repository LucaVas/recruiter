package dev.lucavassos.recruiter.modules.candidate.domain;

import dev.lucavassos.recruiter.modules.candidate.repository.dto.CandidateDto;

public record CandidateResponse(
    String pan,
    CandidateDto candidate
) {

}
