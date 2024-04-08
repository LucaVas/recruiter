package dev.lucavassos.recruiter.modules.job.service;

import dev.lucavassos.recruiter.auth.UserPrincipal;
import dev.lucavassos.recruiter.exception.RequestValidationException;
import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.modules.candidacy.entities.Candidacy;
import dev.lucavassos.recruiter.modules.candidacy.repository.CandidacyRepository;
import dev.lucavassos.recruiter.modules.job.domain.JobResponse;
import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.job.domain.NewJobRequest;
import dev.lucavassos.recruiter.modules.job.domain.UpdateJobRequest;
import dev.lucavassos.recruiter.modules.job.entities.ContractType;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.job.entities.JobHistory;
import dev.lucavassos.recruiter.modules.job.repository.ContractTypeRepository;
import dev.lucavassos.recruiter.modules.job.repository.JobHistoryRepository;
import dev.lucavassos.recruiter.modules.job.repository.JobRepository;
import dev.lucavassos.recruiter.modules.job.repository.dto.FullJobDtoMapper;
import dev.lucavassos.recruiter.modules.job.repository.dto.JobDto;
import dev.lucavassos.recruiter.modules.job.repository.dto.JobDtoMapper;
import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDtoMapper;
import dev.lucavassos.recruiter.modules.skill.entities.Skill;
import dev.lucavassos.recruiter.modules.skill.repository.SkillRepository;
import dev.lucavassos.recruiter.modules.skill.repository.dto.RawSkillDto;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDtoMapper;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private JobHistoryRepository historyRepository;
    @Autowired
    private CandidacyRepository candidacyRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private ContractTypeRepository contractTypeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobDtoMapper jobDtoMapper;
    @Autowired
    private FullJobDtoMapper fullJobDtoMapper;
    @Autowired
    private SkillDtoMapper skillDtoMapper;
    @Autowired
    private QuestionDtoMapper questionDtoMapper;

    @Transactional
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
                            .cvRatePaymentDate(request.getCvRatePaymentDate())
                            .closureBonusPaymentDate(request.getClosureBonusPaymentDate())
                            .comments(request.getComments())
                            .build()
            );
        } catch (Exception e) {
            throw new Exception(e.getCause());
        }


        LOG.info("New job created: [{}]", createdJob);

        // Create new entry in history table
        try {
            historyRepository.save(
                    JobHistory.builder()
                            .status(createdJob.getStatus())
                            .bonusPayPerCv(createdJob.getBonusPayPerCv())
                            .closureBonus(createdJob.getClosureBonus())
                            .job(createdJob)
                            .build()
            );
        } catch (Exception e) {
            throw new Exception(e.getCause());
        }

        return new JobResponse(
                createdJob.getId(),
                jobDtoMapper.apply(createdJob)
        );

    }

    @Transactional
    public JobResponse updateJob(UpdateJobRequest request) throws Exception {

        boolean changes = false;
        Long id = request.id();
        LOG.info("Updating job with id {}", id);

        Job job = repository.findOneById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Job with id [%d] not found.".formatted(id)
                )
        );

        if (request.client() != null && !request.client().equals(job.getClient())) {
            job.setClient(request.client());
            changes = true;
        }
        if (request.name() != null && !request.name().equals(job.getName())) {
            job.setName(request.name());
            changes = true;
        }
        if (request.status() != null && !request.status().equals(job.getStatus())) {
            job.setStatus(request.status());
            changes = true;
        }
        if (request.contractType() != null && !request.contractType().contractTypeName().equals(job.getContractType().getContractTypeName())) {
            ContractType contractType = contractTypeRepository
                    .findByContractTypeName(request.contractType().contractTypeName())
                    .orElseThrow(() -> new BadRequestException(String.format("Contract type %s not available", request.contractType())));

            job.setContractType(contractType);
            changes = true;
        }
        if (request.wantedCvs() != null && !request.wantedCvs().equals(job.getWantedCvs())) {
            job.setWantedCvs(request.wantedCvs());
            changes = true;
        }
        if (request.experienceRangeMin() != null && !request.experienceRangeMin().equals(job.getExperienceRangeMin())) {
            job.setExperienceRangeMin(request.experienceRangeMin());
            changes = true;
        }
        if (request.experienceRangeMax() != null && !request.experienceRangeMax().equals(job.getExperienceRangeMax())) {
            job.setExperienceRangeMax(request.experienceRangeMax());
            changes = true;
        }
        if (request.noticePeriodInDays() != null && !request.noticePeriodInDays().equals(job.getNoticePeriodInDays())) {
            job.setNoticePeriodInDays(request.noticePeriodInDays());
            changes = true;
        }
        if (request.salaryBudget() != null && !request.salaryBudget().equals(job.getSalaryBudget())) {
            job.setSalaryBudget(request.salaryBudget());
            changes = true;
        }
        if (request.currency() != null && !request.currency().equals(job.getCurrency())) {
            job.setCurrency(request.currency());
            changes = true;
        }
        if (request.description() != null && !request.description().equals(job.getDescription())) {
            job.setDescription(request.description());
            changes = true;
        }
        if (request.bonusPayPerCv() != null && !request.bonusPayPerCv().equals(job.getBonusPayPerCv())) {
            job.setBonusPayPerCv(request.bonusPayPerCv());
            changes = true;
        }
        if (request.closureBonus() != null && !request.closureBonus().equals(job.getClosureBonus())) {
            job.setClosureBonus(request.closureBonus());
            changes = true;
        }
        if (request.comments() != null && !request.comments().equals(job.getComments())) {
            job.setComments(request.comments());
            changes = true;
        }
        if (request.skills() != null) {
            Set<Skill> skills = new HashSet<>(skillRepository
                    .findAllById(request.skills().stream().map(RawSkillDto::id).collect(Collectors.toSet())));
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

        // TODO: create new entry in history table if needed


        return new JobResponse(
                job.getId(),
                jobDtoMapper.apply(job)
        );

    }

    @Transactional
    public JobDto getJobById(Long id) {
        Job job = repository.findOneById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Job with id [%d] not found".formatted(id)
                )
        );

        return jobDtoMapper.apply(job);
    }

    @Transactional
    public void changeJobStatus(Long id, JobStatus status) {
        Job job = repository.findOneById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Job with id [%d] not found".formatted(id)
                )
        );

        if (status != null && job.getStatus() != status && job.getStatus() != JobStatus.ARCHIVED) {
            job.setStatus(status);
        }

        repository.save(job);
    }

    @Transactional
    public List<JobDto> getAllJobs() {
        LOG.info("Retrieving {} jobs", 1000);

        List<Job> jobs = repository.findAll();

        List<JobDto> jobsDtos = jobs.stream()
                .filter(job -> job.getStatus() != JobStatus.ARCHIVED)
                .map(job -> {
                    Set<Candidacy> candidacies = new HashSet<>(candidacyRepository.findByJob(job));
                    return fullJobDtoMapper.apply(job, candidacies);
                })
                .toList();

        LOG.info("Jobs retrieved: {}", jobs);

        return jobsDtos;
    }

    @Transactional
    public void deleteJob(Long id) {
        LOG.info("Deleting job {}", id);
        Job job = repository.findOneById(id).orElseThrow(
                () -> {
                    LOG.error("Job {} not found", id);
                    return new ResourceNotFoundException("Job with id %d not found".formatted(id));
                }
        );
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findOneById(userPrincipal.getId()).orElseThrow(
                () -> {
                    LOG.error("User with id {} not found", id);
                    return new ResourceNotFoundException("User not found");
                }
        );
//        if (user.getRoles().stream().anyMatch(role -> role.getName() != RoleName.ROLE_ADMIN)) {
//            LOG.error("User with id {} not found", id);
//            throw new UnauthorizedException("User cannot perform this action");
//        }
        if (job.getStatus() != JobStatus.ARCHIVED) {
            job.setStatus(JobStatus.ARCHIVED);
            Job jobDeleted = repository.save(job);
            LOG.info("Job {} deleted successfully", jobDeleted.getId());
        }
    }
}
