package dev.lucavassos.recruiter.modules.job.service;

import dev.lucavassos.recruiter.exception.RequestValidationException;
import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.modules.job.domain.JobResponse;
import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.job.domain.NewJobRequest;
import dev.lucavassos.recruiter.modules.job.domain.UpdateJobRequest;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.job.repository.JobRepository;
import dev.lucavassos.recruiter.modules.job.repository.dto.JobDto;
import dev.lucavassos.recruiter.modules.job.repository.dto.JobDtoMapper;
import dev.lucavassos.recruiter.modules.skills.Skill;
import dev.lucavassos.recruiter.modules.skills.repository.SkillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class JobService {

    private static final Logger LOG = LoggerFactory.getLogger(JobService.class);

    @Autowired
    private JobRepository repository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private JobDtoMapper dtoMapper;

    public JobResponse addJob(NewJobRequest request) throws Exception {
        LOG.info("Adding new job");

        List<Skill> skills = skillRepository
                .findAllById(request.getSkillsIds());

        Job createdJob;
            try {
                createdJob = repository.save(
                        Job.builder()
                                .client(request.getClient())
                                .name(request.getName())
                                .status(request.getStatus())
                                .wantedCVs(request.getWantedCVs())
                                .skills(skills)
                                .experienceRange(request.getExperienceRange())
                                .noticePeriodInDays(request.getNoticePeriodInDays())
                                .salaryBudget(request.getSalaryBudget())
                                .description(request.getDescription())
                                .bonusPayPerCV(request.getBonusPayPerCV())
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
                    dtoMapper.apply(createdJob)
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
        if (request.getWantedCVs() != null && !request.getWantedCVs().equals(job.getWantedCVs())) {
            job.setWantedCVs(request.getWantedCVs());
            changes = true;
        }
        if (request.getExperienceRange() != null && !request.getExperienceRange().equals(job.getExperienceRange())) {
            job.setExperienceRange(request.getExperienceRange());
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
        if (request.getDescription() != null && !request.getDescription().equals(job.getDescription())) {
            job.setDescription(request.getDescription());
            changes = true;
        }
        if (request.getBonusPayPerCV() != null && !request.getBonusPayPerCV().equals(job.getBonusPayPerCV())) {
            job.setBonusPayPerCV(request.getBonusPayPerCV());
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
        if (request.getSkillsIds() != null) {
            List<Skill> skills = skillRepository
                    .findAllById(request.getSkillsIds());
            if(!skills.equals(job.getSkills())) {
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
                dtoMapper.apply(job)
        );

    }

    public JobDto getJobById(Long id) {
        Job job = repository.findOneById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Job with id [%d] not found".formatted(id)
                )
        );

        return dtoMapper.apply(job);
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

    public List<JobDto> getAllJobs() {
        return repository.findAll()
                .stream()
                .filter(job -> job.getStatus() != JobStatus.ARCHIVED)
                .map(job -> dtoMapper.apply(job))
                .toList();
    }
}
