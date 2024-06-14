package dev.lucavassos.recruiter.modules.job.repository.dto;

import dev.lucavassos.recruiter.modules.client.repository.dto.ClientDtoMapper;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.questionnaire.repository.dto.QuestionnaireDtoMapper;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JobDtoMapper implements Function<Job, JobDto> {

    private QuestionnaireDtoMapper questionnaireDtoMapper;
    private SkillDtoMapper skillMapper;
    private ClientDtoMapper clientDtoMapper;

    @Override
    public JobDto apply(Job job) {
        return JobDto.builder()
                .id(job.getId())
                .client(clientDtoMapper.apply(job.getClient()))
                .name(job.getName())
                .status(job.getStatus())
                .wantedCvs(job.getWantedCvs())
                .skills(job.getSkills().stream().map(skillMapper).toList())
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
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }
}
