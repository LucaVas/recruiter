package dev.lucavassos.recruiter.modules.job.service;

import dev.lucavassos.recruiter.exception.RequestValidationException;
import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.modules.job.domain.JobResponse;
import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.job.domain.NewJobRequest;
import dev.lucavassos.recruiter.modules.job.domain.UpdateJobRequest;
import dev.lucavassos.recruiter.modules.job.entities.ContractType;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.job.repository.ContractTypeRepository;
import dev.lucavassos.recruiter.modules.job.repository.JobRepository;
import dev.lucavassos.recruiter.modules.job.repository.dto.JobDto;
import dev.lucavassos.recruiter.modules.job.repository.dto.JobDtoMapper;
import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDtoMapper;
import dev.lucavassos.recruiter.modules.skill.entities.Skill;
import dev.lucavassos.recruiter.modules.skill.repository.SkillRepository;
import dev.lucavassos.recruiter.modules.skill.repository.dto.RawSkillDto;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDtoMapper;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobService {

    private static final Logger LOG = LoggerFactory.getLogger(JobService.class);

    @Autowired
    private JobRepository repository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private ContractTypeRepository contractTypeRepository;
    @Autowired
    private JobDtoMapper jobDtoMapper;
    @Autowired
    private SkillDtoMapper skillDtoMapper;
    @Autowired
    private QuestionDtoMapper questionDtoMapper;

    public JobResponse addJob(NewJobRequest request) throws Exception {
        LOG.info("Initiating new job creation process...");

        Set<Skill> skills = new HashSet<>(skillRepository
                .findAllById(request.getSkills().stream().map(RawSkillDto::id).collect(Collectors.toSet())));

        ContractType contractType = contractTypeRepository
                .findByContractTypeName(request.getContractType())
                .orElseThrow(() -> new BadRequestException(String.format("Contract type %s not available", request.getContractType())));

        Job createdJob;
        try {
            createdJob = repository.save(
                    Job.builder()
                            .client(request.getClient())
                            .name(request.getName())
                            .status(request.getStatus())
                            .contractType(contractType)
                            .wantedCvs(request.getWantedCvs())
                            .skills(skills)
                            .experienceRangeMin(request.getExperienceRangeMin())
                            .experienceRangeMax(request.getExperienceRangeMax())
                            .noticePeriodInDays(request.getNoticePeriodInDays())
                            .salaryBudget(request.getSalaryBudget())
                            .currency(request.getCurrency())
                            .description(request.getDescription())
                            .bonusPayPerCv(request.getBonusPayPerCv())
                            .closureBonus(request.getClosureBonus())
                            .comments(request.getComments())
                            .build()
            );
        } catch (Exception e) {
            throw new Exception(e.getCause());
        }


        LOG.info("New job created: [{}]", createdJob);

        return new JobResponse(
                createdJob.getId(),
                jobDtoMapper.apply(createdJob)
        );

    }

    public JobResponse updateJob(UpdateJobRequest request) throws Exception {

        boolean changes = false;
        Long id = request.getId();
        LOG.info("Updating job with id {}", id);

        Job job = repository.findOneById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Job with id [%d] not found.".formatted(id)
                )
        );

        if (request.getClient() != null && !request.getClient().equals(job.getClient())) {
            job.setClient(request.getClient());
            changes = true;
        }
        if (request.getName() != null && !request.getName().equals(job.getName())) {
            job.setName(request.getName());
            changes = true;
        }
        if (request.getStatus() != null && !request.getStatus().equals(job.getStatus())) {
            job.setStatus(request.getStatus());
            changes = true;
        }
        if (request.getContractType() != null && !request.getContractType().equals(job.getContractType().getContractTypeName())) {
            ContractType contractType = contractTypeRepository
                    .findByContractTypeName(request.getContractType())
                    .orElseThrow(() -> new BadRequestException(String.format("Contract type %s not available", request.getContractType())));

            job.setContractType(contractType);
            changes = true;
        }
        if (request.getWantedCvs() != null && !request.getWantedCvs().equals(job.getWantedCvs())) {
            job.setWantedCvs(request.getWantedCvs());
            changes = true;
        }
        if (request.getExperienceRangeMin() != null && !request.getExperienceRangeMin().equals(job.getExperienceRangeMin())) {
            job.setExperienceRangeMin(request.getExperienceRangeMin());
            changes = true;
        }
        if (request.getExperienceRangeMax() != null && !request.getExperienceRangeMax().equals(job.getExperienceRangeMax())) {
            job.setExperienceRangeMax(request.getExperienceRangeMax());
            changes = true;
        }
        if (request.getNoticePeriodInDays() != null && !request.getNoticePeriodInDays().equals(job.getNoticePeriodInDays())) {
            job.setNoticePeriodInDays(request.getNoticePeriodInDays());
            changes = true;
        }
        if (request.getSalaryBudget() != null && !request.getSalaryBudget().equals(job.getSalaryBudget())) {
            job.setSalaryBudget(request.getSalaryBudget());
            changes = true;
        }
        if (request.getCurrency() != null && !request.getCurrency().equals(job.getCurrency())) {
            job.setCurrency(request.getCurrency());
            changes = true;
        }
        if (request.getDescription() != null && !request.getDescription().equals(job.getDescription())) {
            job.setDescription(request.getDescription());
            changes = true;
        }
        if (request.getBonusPayPerCv() != null && !request.getBonusPayPerCv().equals(job.getBonusPayPerCv())) {
            job.setBonusPayPerCv(request.getBonusPayPerCv());
            changes = true;
        }
        if (request.getClosureBonus() != null && !request.getClosureBonus().equals(job.getClosureBonus())) {
            job.setClosureBonus(request.getClosureBonus());
            changes = true;
        }
        if (request.getComments() != null && !request.getComments().equals(job.getComments())) {
            job.setComments(request.getComments());
            changes = true;
        }
        if (request.getSkills() != null) {
            Set<Skill> skills = new HashSet<>(skillRepository
                    .findAllById(request.getSkills().stream().map(RawSkillDto::id).collect(Collectors.toSet())));
            if (!skills.equals(job.getSkills())) {
                job.setSkills(skills);
                changes = true;
            }
        }

        if (!changes) {
            throw new RequestValidationException("No updates were made to data.");
        }

        try {
            repository.save(job);
        } catch (Exception e) {
            throw new Exception(e.getCause());
        }


        LOG.info("Job updated: [{}]", job);

        return new JobResponse(
                job.getId(),
                jobDtoMapper.apply(job)
        );

    }

    public JobDto getJobById(Long id) {
        Job job = repository.findOneById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Job with id [%d] not found".formatted(id)
                )
        );

        return jobDtoMapper.apply(job);
    }

    public void changeJobStatus(Long id, JobStatus status) {
        Job job = repository.findOneById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Job with id [%d] not found".formatted(id)
                )
        );

        if (status != null && job.getStatus() != status) {
            job.setStatus(status);
        }

        repository.save(job);
    }

    @Transactional
    public List<JobDto> getAllJobs() {
        LOG.info("Retrieving {} jobs", 1000);

        List<JobDto> jobs =
                repository.findAll()
                        .stream()
                        .map(job -> jobDtoMapper.apply(job)
                        )
                        .collect(Collectors.toList());

        LOG.info("Jobs retrieved: {}", jobs);

        return jobs;
    }
}
