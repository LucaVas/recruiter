package dev.lucavassos.recruiter.modules.job.domain;


import dev.lucavassos.recruiter.modules.skills.Skill;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
public class UpdateJobRequest extends NewJobRequest {

    private final Long id;

    UpdateJobRequest(Long id, String client, String name, JobStatus status, Integer wantedCVs, List<Long> skillsIds, Double experienceRange, Integer noticePeriodInDays, Double salaryBudget, String description, Double bonusPayPerCV, String closureBonus, String comments) {
        super(client, name, status, wantedCVs, skillsIds, experienceRange, noticePeriodInDays, salaryBudget, description, bonusPayPerCV, closureBonus, comments);
        this.id = id;
    }
}
