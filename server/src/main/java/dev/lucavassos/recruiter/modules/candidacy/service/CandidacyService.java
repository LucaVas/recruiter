package dev.lucavassos.recruiter.modules.candidacy.service;

import dev.lucavassos.recruiter.auth.UserPrincipal;
import dev.lucavassos.recruiter.exception.DatabaseException;
import dev.lucavassos.recruiter.exception.RequestValidationException;
import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.exception.UnauthorizedException;
import dev.lucavassos.recruiter.modules.candidacy.domain.CandidacyResponse;
import dev.lucavassos.recruiter.modules.candidacy.domain.NewCandidacyRequest;
import dev.lucavassos.recruiter.modules.candidacy.domain.UpdateCandidacyRequest;
import dev.lucavassos.recruiter.modules.candidacy.entities.Candidacy;
import dev.lucavassos.recruiter.modules.candidacy.entities.CandidacyId;
import dev.lucavassos.recruiter.modules.candidacy.repository.dto.CandidacyDto;
import dev.lucavassos.recruiter.modules.candidacy.repository.dto.CandidacyDtoMapper;
import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import dev.lucavassos.recruiter.modules.candidate.repository.CandidateRepository;
import dev.lucavassos.recruiter.modules.candidacy.repository.CandidacyRepository;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.job.repository.JobRepository;
import dev.lucavassos.recruiter.modules.user.domain.RoleName;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import dev.lucavassos.recruiter.monitoring.MonitoringProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CandidacyService {

    private static final Logger LOG = LoggerFactory.getLogger(CandidacyService.class);

    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private CandidacyRepository candidacyRepository;
    @Autowired
    private CandidacyDtoMapper candidacyDtoMapper;
    @Autowired
    MonitoringProcessor monitoringProcessor;

    @Transactional
    public void addCandidacy(NewCandidacyRequest candidacy) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();


        // find recruiter
        User recruiter = userRepository.findOneById(userPrincipal.getId()).orElseThrow(
                () -> {
                    LOG.error("Recruiter with ID {} not found", userPrincipal.getId());
                    return new ResourceNotFoundException("Recruiter not found");
                }
        );
        // find job
        Job job = jobRepository.findOneById(candidacy.jobId()).orElseThrow(
                () -> {
                    LOG.error("Job with ID {} not found", candidacy.jobId());
                    return new ResourceNotFoundException("Job not found");
                }
        );
        // find candidate
        Candidate candidate = candidateRepository.findOneByPan(candidacy.candidatePan()).orElseThrow(
                () -> {
                    LOG.error("Candidate with Pan {} not found", candidacy.candidatePan());
                    return new ResourceNotFoundException("Candidate not found");
                }
        );

        if (candidacyRepository.existsByJobAndCandidate(job, candidate)) {
            LOG.error("Candidacy already exists for job {} and candidate {}", job, candidate);
            throw new RequestValidationException("Candidacy already exists");
        }

        CandidacyId candidacyId = new CandidacyId(candidate.getPan(), job.getId());
        Candidacy newCandidacy = Candidacy.builder()
                .id(candidacyId)
                .job(job)
                .candidate(candidate)
                .recruiter(recruiter)
                .relevantExperience(candidacy.relevantExperience())
                .expectedCtc(candidacy.expectedCtc())
                .officialNoticePeriod(candidacy.officialNoticePeriod())
                .actualNoticePeriod(candidacy.actualNoticePeriod())
                .reasonForQuickJoin(candidacy.reasonForQuickJoin())
                .remarks(candidacy.remarks())
                .build();

        candidacyRepository.save(newCandidacy);
        monitoringProcessor.incrementCandidaciesCounter();

    }

    @Transactional
    public CandidacyResponse getCandidacy(Long jobId, String pan) {

        Job job = jobRepository.findOneById(jobId).orElseThrow(
                () -> {
                    LOG.error("Job with id {} not found", jobId);
                    return new ResourceNotFoundException("Job not found");
                }
        );
        Candidate candidate = candidateRepository.findOneByPan(pan).orElseThrow(
                () -> {
                    LOG.error("Candidate with pan {} not found", pan);
                    return new ResourceNotFoundException("Candidate not found");
                }
        );

        return candidacyRepository.findByJobAndCandidate(job, candidate)
                .map(candidacy ->
                        new CandidacyResponse(candidacyDtoMapper.apply(candidacy))
        ).orElseThrow(
                () -> {
                    LOG.error("Candidacy with job {} and candidate {} not found", job, candidate);
                    return new ResourceNotFoundException("Candidacy not found");
                }
        );
    }

    @Transactional
    public List<CandidacyDto> getAllCandidacies() {

        User user = getAuthUser();
        List<Candidacy> candidacies = candidacyRepository.findAll();

        return candidacies
                .stream()
                .filter(candidacy -> user.isAdmin() || candidacy.getRecruiter().getId().equals(user.getId()))
                .map(candidacy -> candidacyDtoMapper.apply(candidacy))
                .toList();
    }

    @Transactional
    public CandidacyResponse updateCandidacy(Long jobId, String pan, UpdateCandidacyRequest request) {

        boolean changes = false;
        LOG.info("Updating candidacy with jobId {} and pan {}", jobId, pan);

        Job job = jobRepository.findOneById(jobId).orElseThrow(
                () -> {
                    LOG.error("Job with id [{}] not found.", jobId);
                    return new ResourceNotFoundException(
                            "Job not found.");
                }
        );

        Candidate candidate = candidateRepository.findOneByPan(pan).orElseThrow(
                () -> {
                    LOG.error("Candidate with pan [{}] not found.", pan);
                    return new ResourceNotFoundException(
                            "Candidate not found.");
                }
        );

        Candidacy candidacy = candidacyRepository.findByJobAndCandidate(job, candidate).orElseThrow(
                () -> {
                    LOG.error("Candidacy with job ID {} and candidate pan {} not found.", jobId, pan);
                    return new ResourceNotFoundException(
                            "Candidacy not found.");
                }
        );

        User user = getAuthUser();
        if (!isUserAuthorized(user, candidacy)) {
            LOG.error("User with id {} is not authorized to modify this candidacy", user.getId());
            throw new UnauthorizedException("Recruiter is unauthorized to modify this candidacy");
        }

        if (request.relevantExperience() != null && request.relevantExperience() != candidacy.getRelevantExperience()) {
            candidacy.setRelevantExperience(request.relevantExperience());
            changes = true;
        }

        if (request.expectedCtc() != null && request.expectedCtc() != candidacy.getExpectedCtc()) {
            candidacy.setExpectedCtc(request.expectedCtc());
            changes = true;
        }

        if (request.officialNoticePeriod() != null && request.officialNoticePeriod() != candidacy.getOfficialNoticePeriod()) {
            candidacy.setOfficialNoticePeriod(request.officialNoticePeriod());
            changes = true;
        }

        if (request.actualNoticePeriod() != null && request.actualNoticePeriod() != candidacy.getActualNoticePeriod()) {
            candidacy.setActualNoticePeriod(request.actualNoticePeriod());
            changes = true;
        }

        if (request.reasonForQuickJoin() != null && !request.reasonForQuickJoin().equals(candidacy.getReasonForQuickJoin())) {
            candidacy.setReasonForQuickJoin(request.reasonForQuickJoin());
            changes = true;
        }

        if (request.remarks() != null && !request.remarks().equals(candidacy.getRemarks())) {
            candidacy.setRemarks(request.remarks());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No updates were made to data.");
        }
        try {
            candidacyRepository.save(candidacy);
        } catch (Exception e) {
            LOG.error("Database error while updating candidacy: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }

        LOG.info("Candidacy updated: [{}]", candidacy);

        return new CandidacyResponse(
                candidacyDtoMapper.apply(candidacy)
        );

    }

    private User getAuthUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userRepository.findOneById(userPrincipal.getId()).orElseThrow(
                () -> {
                    LOG.error("User with id {} not found", userPrincipal.getId());
                    return new ResourceNotFoundException("User not found");
                }
        );
    }

    private boolean isUserAuthorized(User recruiter, Candidacy candidacy) {
        return recruiter.getRoleName() == RoleName.ROLE_ADMIN ||
                candidacy.getRecruiter().getId().equals(recruiter.getId());
    }

}
