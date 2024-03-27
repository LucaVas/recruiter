package dev.lucavassos.recruiter.modules.job.repository.dto;

import dev.lucavassos.recruiter.modules.job.domain.JobStatus;

import java.time.LocalDateTime;
import java.util.List;

public record JobDto(
        Long id,
        String client,
        String name,
        JobStatus status,
        Integer wantedCVs,
        List<SkillDto> skills,
        // TODO: Add type of contract - permanent or temporary
        Double experienceRange,
        Integer noticePeriodInDays,
        Double salaryBudget,
        String description,
        Double bonusPayPerCV,
        String closureBonus,
        String comments,
        // Add numberOfCandidates as calculation
        LocalDateTime creationDate
) {
}
