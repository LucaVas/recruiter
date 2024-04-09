package dev.lucavassos.recruiter.modules.job.repository.dto;

import dev.lucavassos.recruiter.modules.candidacy.entities.Candidacy;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDto;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDto;
import dev.lucavassos.recruiter.modules.user.repository.dto.RecruiterDto;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FullJobDtoMapper implements BiFunction<Job, Set<Candidacy>, JobDto> {
    @Override
    public JobDto apply(Job job, Set<Candidacy> candidacies) {
        return new JobDto(
                job.getId(),
                job.getClient(),
                job.getName(),
                job.getStatus(),
                job.getWantedCvs(),
                job.getSkills().stream()
                        .map(skill -> new SkillDto(
                                skill.getId(),
                                skill.getName(),
                                skill.getQuestions().stream()
                                        .map(question -> new QuestionDto(
                                                question.getId(),
                                                question.getText()
                                        )).collect(Collectors.toSet())
                        ))
                        .collect(Collectors.toSet()),
                new ContractTypeDto(job.getContractType().getContractTypeName()),
                job.getExperienceRangeMin(),
                job.getExperienceRangeMax(),
                job.getNoticePeriodInDays(),
                job.getSalaryBudget(),
                job.getCurrency(),
                job.getDescription(),
                job.getBonusPayPerCv(),
                job.getClosureBonus(),
                job.getComments(),
                candidacies.size(),
                job.getClosureBonusPaymentDate(),
                job.getCvRatePaymentDate(),
                job.getCreatedAt()
        );
    }
}
