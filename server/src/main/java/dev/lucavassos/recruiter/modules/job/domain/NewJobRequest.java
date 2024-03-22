package dev.lucavassos.recruiter.modules.job.domain;

import dev.lucavassos.recruiter.modules.skills.Skill;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class NewJobRequest {
    private String client;
    private String name;
    private JobStatus status;
    private Integer wantedCVs;
    private List<Long> skillsIds;
    private Double experienceRange;
    private Integer noticePeriodInDays;
    private Double salaryBudget;
    private String description;
    private Double bonusPayPerCV;
    private String closureBonus;
    private String comments;
}
