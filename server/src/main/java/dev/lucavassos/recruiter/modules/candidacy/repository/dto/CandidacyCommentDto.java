package dev.lucavassos.recruiter.modules.candidacy.repository.dto;

import dev.lucavassos.recruiter.modules.candidate.repository.dto.CandidateDto;
import dev.lucavassos.recruiter.modules.job.repository.dto.JobDto;
import dev.lucavassos.recruiter.modules.user.repository.dto.RecruiterDto;

import java.time.LocalDateTime;

public record CandidacyCommentDto(
        Long id,
        String text,
        LocalDateTime createdDTime,
        LocalDateTime modifiedDTime
) {
}
