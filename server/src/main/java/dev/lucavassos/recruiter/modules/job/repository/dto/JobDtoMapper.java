package dev.lucavassos.recruiter.modules.job.repository.dto;

import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDto;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JobDtoMapper implements Function<Job, JobDto> {
    @Override
    public JobDto apply(Job job) {
        return new JobDto(
                job.getId(),
                job.getClient(),
                job.getName(),
                job.getStatus(),
                job.getWantedCvs(),
                job.getSkills().stream().map(skill -> new SkillDto(skill.getId(), skill.getName(), skill.getCreatedDTime(), skill.getModifiedDTime())).collect(Collectors.toList()),
                job.getContractType(),
                job.getExperienceRangeMin(),
                job.getExperienceRangeMax(),
                job.getNoticePeriodInDays(),
                job.getSalaryBudget(),
                job.getCurrency(),
                job.getDescription(),
                job.getBonusPayPerCv(),
                job.getClosureBonus(),
                job.getCvRatePaymentDate(),
                job.getClosureBonusPaymentDate(),
                job.getNumberOfCandidates(),
                job.getCreatedDTime(),
                job.getModifiedDTime()
        );
    }
}
