package dev.lucavassos.recruiter.modules.job.repository.dto;

import dev.lucavassos.recruiter.modules.job.domain.Currency;
import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDto;

import java.time.LocalDateTime;
import java.util.Set;

public record JobDto(
        Long id,
        String client,
        String name,
        JobStatus status,
        Integer wantedCvs,
        Set<SkillDto> skills,
        ContractTypeDto contractType,
        Integer experienceRangeMin,
        Integer experienceRangeMax,
        Integer noticePeriodInDays,
        Double salaryBudget,
        Currency currency,
        String description,
        Double bonusPayPerCV,
        String closureBonus,
        String comments,
        Integer numberOfCandidates,
        LocalDateTime closureBonusPaymentDate,
        LocalDateTime cvRatePaymentDate,
        LocalDateTime createdAt
) {
}
