package dev.lucavassos.recruiter.modules.job.repository.dto;

import dev.lucavassos.recruiter.modules.job.domain.Currency;
import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.skill.repository.dto.RawSkillDto;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDto;
import dev.lucavassos.recruiter.modules.user.repository.dto.RecruiterDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record JobDto(
        Long id,
        String client,
        String name,
        JobStatus status,
        Integer wantedCvs,
        List<RawSkillDto> skills,
        ContractTypeDto contractType,
        Integer experienceRangeMin,
        Integer experienceRangeMax,
        Integer noticePeriodInDays,
        Double salaryBudget,
        Currency currency,
        String description,
        Double bonusPayPerCv,
        Double closureBonus,
        String comments,
        Integer numberOfCandidates,
        LocalDateTime closureBonusPaymentDate,
        LocalDateTime cvRatePaymentDate,
        LocalDateTime createdAt
) {
}
