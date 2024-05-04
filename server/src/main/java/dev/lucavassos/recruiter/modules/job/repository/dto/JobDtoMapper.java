package dev.lucavassos.recruiter.modules.job.repository.dto;

import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDtoMapper;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class JobDtoMapper implements Function<Job, JobDto> {

    @Autowired
    private QuestionDtoMapper questionMapper;

    @Autowired
    private SkillDtoMapper skillMapper;

    @Override
    public JobDto apply(Job job) {
        return new JobDto(
                job.getId(),
                job.getClient(),
                job.getName(),
                job.getStatus(),
                job.getWantedCvs(),
                job.getSkills().stream().map(skillMapper).toList(),
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
                job.getQuestions().stream().map(questionMapper).toList(),
                job.getCreatedDTime(),
                job.getModifiedDTime()
        );
    }
}
