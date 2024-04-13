package dev.lucavassos.recruiter.modules.job.domain;

import dev.lucavassos.recruiter.modules.job.repository.dto.ContractTypeDto;
import dev.lucavassos.recruiter.modules.skill.repository.dto.RawSkillDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

public record NewJobRequest (
        String client,
        String name,
        JobStatus status,
        ContractTypeDto contractType,
        Integer wantedCvs,
        Integer experienceRangeMin,
        Integer experienceRangeMax,
        Integer noticePeriodInDays,
        List<RawSkillDto> skills,
        Double salaryBudget,
        Currency currency,
        String description,
        Double bonusPayPerCv,
        Double closureBonus,
        LocalDateTime closureBonusPaymentDate,
        LocalDateTime cvRatePaymentDate,
        String comments
){}
