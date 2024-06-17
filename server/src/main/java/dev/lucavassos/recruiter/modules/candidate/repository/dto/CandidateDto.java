package dev.lucavassos.recruiter.modules.candidate.repository.dto;

import dev.lucavassos.recruiter.modules.candidate.domain.CandidateStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public final class CandidateDto {
    private final String pan;
    private final String name;
    private final String phone;
    private final String email;
    private final Double totalExperience;
    private final String education;
    private final Double currentCtc;
    private final CandidateStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
