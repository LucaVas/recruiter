package dev.lucavassos.recruiter.modules.job.repository.dto;

import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.job.entities.ContractType;
import dev.lucavassos.recruiter.modules.skills.repository.dto.SkillDto;

import java.time.LocalDateTime;
import java.util.Set;

public record JobDto(
        Long id,
        String client,
        String name,
        JobStatus status,
        Integer wantedCVs,
        Set<SkillDto> skills,
        ContractTypeDto contractType,
        String experienceRange,
        Integer noticePeriodInDays,
        Double salaryBudget,
        String description,
        Double bonusPayPerCV,
        String closureBonus,
        String comments,
        Integer numberOfCandidates,
        LocalDateTime createdAt
) {
}
