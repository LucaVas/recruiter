package dev.lucavassos.recruiter.modules.candidacy.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@Builder
public final class CandidacyFileDto {
    private final Long id;
    private final String type;
    private final String name;
    private final UUID uniqueId;
    private final LocalDateTime createdDTime;
    private final LocalDateTime modifiedDTime;
}
