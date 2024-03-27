package dev.lucavassos.recruiter.modules.job.repository.dto;

import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDto;
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
                job.getWantedCVs(),
                job.getSkills().stream()
                        .map(skill -> new SkillDto(
                                skill.getId(),
                                skill.getName(),
                                skill.getQuestions().stream()
                                        .map(question -> new QuestionDto(
                                                question.getId(),
                                                question.getText()
                                        )).collect(Collectors.toList())
                        ))
                        .collect(Collectors.toList()),
                job.getExperienceRange(),
                job.getNoticePeriodInDays(),
                job.getSalaryBudget(),
                job.getDescription(),
                job.getBonusPayPerCV(),
                job.getClosureBonus(),
                job.getComments(),
                job.getCreatedAt()
        );
    }
}
