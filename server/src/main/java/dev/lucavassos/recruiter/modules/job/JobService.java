package dev.lucavassos.recruiter.modules.job;

import dev.lucavassos.recruiter.exception.DatabaseException;
import dev.lucavassos.recruiter.exception.RequestValidationException;
import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.modules.candidacy.repository.CandidacyRepository;
import dev.lucavassos.recruiter.modules.client.entities.Client;
import dev.lucavassos.recruiter.modules.client.repository.ClientRepository;
import dev.lucavassos.recruiter.modules.job.domain.*;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.job.entities.JobHistory;
import dev.lucavassos.recruiter.modules.job.repository.JobHistoryRepository;
import dev.lucavassos.recruiter.modules.job.repository.JobRepository;
import dev.lucavassos.recruiter.modules.job.repository.dto.JobDto;
import dev.lucavassos.recruiter.modules.job.repository.dto.JobDtoMapper;
import dev.lucavassos.recruiter.modules.question.entity.Question;
import dev.lucavassos.recruiter.modules.question.repository.QuestionRepository;
import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDto;
import dev.lucavassos.recruiter.modules.skill.entities.Skill;
import dev.lucavassos.recruiter.modules.skill.repository.SkillRepository;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDto;
import dev.lucavassos.recruiter.modules.user.domain.RoleName;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import dev.lucavassos.recruiter.monitoring.MonitoringProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class JobService {

    private final JobRepository repository;
    private final JobHistoryRepository historyRepository;
    private final CandidacyRepository candidacyRepository;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final JobDtoMapper jobDtoMapper;
    private final MonitoringProcessor monitoringProcessor;
    private final ClientRepository clientRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public JobResponse addJob(NewJobRequest request) {
        log.debug("Initiating new job creation process...");

        List<Skill> skills = skillRepository
                .findAllById(request.skills().stream().map(SkillDto::id).collect(Collectors.toList()));

        log.debug("Skills found: {}", skills);

        Client client = clientRepository
                .findByName(request.client().name())
                .orElse(
                        clientRepository.save(
                                Client.builder()
                                        .name(request.client().name())
                                        .industry(request.client().industry())
                                        .build()
                        ));

        List<Question> questions = questionRepository
                .findAllById(request.questions().stream().map(QuestionDto::id).collect(Collectors.toList()));

        User recruiter = getAuthUser();
        Job job = Job.builder()
                .client(client)
                .name(request.name())
                .status(request.status())
                .contractType(request.contractType())
                .wantedCvs(request.wantedCvs())
                .skills(skills)
                .experienceRangeMin(request.experienceRangeMin())
                .experienceRangeMax(request.experienceRangeMax())
                .noticePeriodInDays(request.noticePeriodInDays())
                .salaryBudget(request.salaryBudget())
                .currency(request.currency())
                .description(request.description())
                .bonusPayPerCv(request.bonusPayPerCv())
                .closureBonus(request.closureBonus())
                .cvRatePaymentDate(request.cvRatePaymentDate())
                .closureBonusPaymentDate(request.closureBonusPaymentDate())
                .recruiter(recruiter)
                .questions(questions)
                .build();
        Job createdJob = saveJob(job);
        saveJobInHistoryTable(createdJob, recruiter);

        log.debug("New job created: [{}]", createdJob);
        monitoringProcessor.incrementJobsCounter();

        return new JobResponse(
                createdJob.getId(),
                jobDtoMapper.apply(createdJob)
        );
    }

    @Transactional
    public JobResponse updateJob(UpdateJobRequest request) {

        boolean changes = false;
        Long id = request.id();
        log.debug("Updating job with id {}", id);

        Job job = repository
                .findOneByIdAndStatusNot(id, JobStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found."));
        Client client = clientRepository
                .findById(request.client().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        User recruiter = getAuthUser();
        if (!isUserAuthorized(recruiter, job)) {
            log.error("User with id {} is not authorized to modify this job", recruiter.getId());
            throw new AccessDeniedException("Recruiter is unauthorized to modify this job");
        }


        if (!request.client().equals(job.getClient())) {
            job.setClient(client);
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
        if (request.contractType() != null && !request.contractType().equals(job.getContractType())) {
            job.setContractType(request.contractType());
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
        if (request.closureBonusPaymentDate() != null && !request.closureBonusPaymentDate().equals(job.getClosureBonusPaymentDate())) {
            job.setClosureBonusPaymentDate(request.closureBonusPaymentDate());
            changes = true;
        }
        if (request.cvRatePaymentDate() != null && !request.cvRatePaymentDate().equals(job.getCvRatePaymentDate())) {
            job.setCvRatePaymentDate(request.cvRatePaymentDate());
            changes = true;
        }
        if (request.skills() != null) {
            List<Skill> skills = skillRepository
                    .findAllById(request.skills().stream().map(SkillDto::id).collect(Collectors.toList()));
            if (!skills.equals(job.getSkills())) {
                job.setSkills(skills);
                changes = true;
            }
        }

        if (!changes) {
            throw new RequestValidationException("No updates were made to data.");
        }
        saveJob(job);
        saveJobInHistoryTable(job, recruiter);

        log.debug("Job updated: [{}]", job);
        return new JobResponse(
                job.getId(),
                jobDtoMapper.apply(job)
        );

    }

    @Transactional
    public JobDto getJobById(Long id) {
        Job job = repository
                .findOneByIdAndStatusNot(id, JobStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        User user = getAuthUser();
        if (job.getStatus() == JobStatus.ARCHIVED && user.getRoleName() != RoleName.ADMIN) {
            throw new AccessDeniedException("Job is not accessible");
        }

        log.debug("Job retrieved: [{}]", job);
        return jobDtoMapper.apply(job);
    }

    @Transactional
    public void changeJobStatus(Long id, ChangeJobStatusRequest request) {
        Job job = repository.findOneByIdAndStatusNot(id, JobStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        User user = getAuthUser();
        if (request.status() != null && job.getStatus() != request.status()) {
            job.setStatus(request.status());
            saveJobInHistoryTable(job, user);
        }
        saveJob(job);
    }

    @Transactional
    public List<JobDto> getAllJobs(Integer pageNumber, Integer pageSize) {
        Pageable limit = PageRequest.of(pageNumber, pageSize);
        log.debug("Retrieving {} jobs", 1000);

        List<Job> jobs = repository.findAllByStatusNot(JobStatus.DELETED, limit);

        log.info("Jobs retrieved: {}", jobs);

        User user = getAuthUser();
        List<JobDto> jobDtos = jobs.stream()
                .filter(job -> user.getRoleName() == RoleName.ADMIN || job.getStatus() != JobStatus.ARCHIVED)
                .peek(job -> job.setNumberOfCandidates(candidacyRepository.findByJob(job).size()))
                .map(jobDtoMapper)
                .toList();

        log.debug("Jobs retrieved: {}", jobs);
        return jobDtos;
    }

    @Transactional
    public void deleteJob(Long id) {
        log.debug("Deleting job {}", id);
        Job job = repository
                .findOneByIdAndStatusNot(id, JobStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        User user = getAuthUser();
        job.setStatus(JobStatus.DELETED);
        Job jobDeleted = saveJob(job);
        log.debug("Job {} deleted successfully", jobDeleted.getId());
        saveJobInHistoryTable(job, user);
    }

    private User getAuthUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User userPrincipal = (User) authentication.getPrincipal();
        return userRepository.findOneById(userPrincipal.getId()).orElseThrow(
                () -> {
                    log.error("User with id {} not found", userPrincipal.getId());
                    return new ResourceNotFoundException("User not found");
                }
        );
    }

    private boolean isUserAuthorized(User recruiter, Job job) {
        return recruiter.getRoleName() == RoleName.ADMIN ||
                job.getRecruiter().getId().equals(recruiter.getId());
    }

    private Job saveJob(Job job) {
        try {
            return repository.save(job);
        } catch (Exception e) {
            log.error("Error while saving job: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }

    }

    private void saveJobInHistoryTable(Job job, User user) {
        try {
            // Create new entry in history table
            historyRepository.save(
                    JobHistory.builder()
                            .status(job.getStatus())
                            .bonusPayPerCv(job.getBonusPayPerCv())
                            .closureBonus(job.getClosureBonus())
                            .job(job)
                            .modifiedBy(user)
                            .build()
            );
        } catch (Exception e) {
            log.error("Error while saving job in history table: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }

    }
}



