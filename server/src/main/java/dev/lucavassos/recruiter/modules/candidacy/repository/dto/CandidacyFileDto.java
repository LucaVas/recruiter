package dev.lucavassos.recruiter.modules.candidacy.repository.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CandidacyFileDto(
        Long id,
        String type,
        String name,
        UUID uniqueId,
        LocalDateTime createdDTime,
        LocalDateTime modifiedDTime
) {
}
