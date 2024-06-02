package dev.lucavassos.recruiter.modules.job.repository.dto;

import dev.lucavassos.recruiter.modules.client.repository.dto.ClientDtoMapper;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.questionnaire.repository.dto.QuestionnaireDtoMapper;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class JobDtoMapper implements Function<Job, JobDto> {

    @Autowired
    private QuestionnaireDtoMapper questionnaireDtoMapper;

    @Autowired
    private SkillDtoMapper skillMapper;

    @Autowired
    private ClientDtoMapper clientDtoMapper;

    @Override
    public JobDto apply(Job job) {
        return new JobDto(
                job.getId(),
                clientDtoMapper.apply(job.getClient()),
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
                questionnaireDtoMapper.apply(job.getQuestionnaire()),
                job.getCreatedAt(),
                job.getUpdatedAt()
        );
    }
}
