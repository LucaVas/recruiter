package dev.lucavassos.recruiter.modules.job.domain;

import dev.lucavassos.recruiter.modules.job.entities.ContractType;
import dev.lucavassos.recruiter.modules.job.repository.dto.ContractTypeDto;
import dev.lucavassos.recruiter.modules.skill.repository.dto.RawSkillDto;
import dev.lucavassos.recruiter.modules.user.entities.ContractTypeName;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class NewJobRequest {
    private String client;
    private String name;
    private JobStatus status;
    private ContractTypeName contractType;
    private Integer wantedCvs;
    private Integer experienceRangeMin;
    private Integer experienceRangeMax;
    private Integer noticePeriodInDays;
    private List<RawSkillDto> skills;
    private Double salaryBudget;
    private Currency currency;
    private String description;
    private Double bonusPayPerCv;
    private String closureBonus;
    private String comments;
}
