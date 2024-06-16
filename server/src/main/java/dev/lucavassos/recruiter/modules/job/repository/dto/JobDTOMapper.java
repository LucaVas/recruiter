package dev.lucavassos.recruiter.modules.job.repository.dto;

import dev.lucavassos.recruiter.modules.client.repository.dto.ClientDtoMapper;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.questionnaire.repository.dto.QuestionnaireDtoMapper;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class JobDTOMapper implements Function<Job, JobDTO> {

    @Autowired
    private QuestionnaireDtoMapper questionnaireDtoMapper;
    @Autowired
    private SkillDtoMapper skillMapper;
    @Autowired
    private ClientDtoMapper clientDtoMapper;

    @Override
    public JobDTO apply(Job job) {
        job.getClient();
        job.getSkills().size();
        job.getQuestionnaire();

        return JobDTO.builder()
                .id(job.getId())
                .client(clientDtoMapper.apply(job.getClient()))
                .name(job.getName())
                .status(job.getStatus())
                .wantedCvs(job.getWantedCvs())
                .contractType(job.getContractType())
                .experienceRangeMin(job.getExperienceRangeMin())
                .experienceRangeMax(job.getExperienceRangeMax())
                .noticePeriodInDays(job.getNoticePeriodInDays())
                .salaryBudget(job.getSalaryBudget())
                .currency(job.getCurrency())
                .description(job.getDescription())
                .bonusPayPerCv(job.getBonusPayPerCv())
                .closureBonus(job.getClosureBonus())
                .cvRatePaymentDate(job.getCvRatePaymentDate())
                .closureBonusPaymentDate(job.getClosureBonusPaymentDate())
                .numberOfCandidates(job.getNumberOfCandidates())
                .skills(job.getSkills().stream().map(skillMapper).toList())
                .questionnaire(questionnaireDtoMapper.apply(job.getQuestionnaire()))
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }
}
