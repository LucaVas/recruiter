package dev.lucavassos.recruiter.modules.job.domain;

import dev.lucavassos.recruiter.modules.job.entities.ContractType;
import dev.lucavassos.recruiter.modules.job.repository.dto.ContractTypeDto;
import dev.lucavassos.recruiter.modules.skill.repository.dto.RawSkillDto;
import dev.lucavassos.recruiter.modules.user.entities.ContractTypeName;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class UpdateJobRequest extends NewJobRequest {

    private final Long id;

    UpdateJobRequest(Long id, String client, String name, JobStatus status, ContractTypeName contractType, Integer wantedCvs, Integer experienceRangeMin, Integer experienceRangeMax, Integer noticePeriodInDays, List<RawSkillDto> rawSkills, Double salaryBudget, Currency currency, String description, Double bonusPayPerCv, String closureBonus, LocalDateTime closureBonusPaymentDate, LocalDateTime cvRatePaymentDate, String comments) {
        super(client, name, status, contractType, wantedCvs, experienceRangeMin, experienceRangeMax, noticePeriodInDays, rawSkills, salaryBudget, currency, description, bonusPayPerCv, closureBonus, closureBonusPaymentDate, cvRatePaymentDate, comments);
        this.id = id;
    }
}
