package dev.lucavassos.recruiter.modules.job.repository.dto;

import dev.lucavassos.recruiter.modules.job.domain.JobStatus;

import java.time.LocalDateTime;

public record JobHistoryDto(
        Long id,
        JobStatus status,
        Double bonusPayPerCV,
        String closureBonus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
