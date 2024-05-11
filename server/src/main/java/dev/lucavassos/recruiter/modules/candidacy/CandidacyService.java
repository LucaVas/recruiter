package dev.lucavassos.recruiter.modules.candidacy;

import dev.lucavassos.recruiter.auth.UserPrincipal;
import dev.lucavassos.recruiter.exception.*;
import dev.lucavassos.recruiter.modules.candidacy.domain.CandidacyStatus;
import dev.lucavassos.recruiter.modules.candidacy.domain.NewCandidacyCommentRequest;
import dev.lucavassos.recruiter.modules.candidacy.domain.NewCandidacyRequest;
import dev.lucavassos.recruiter.modules.candidacy.domain.UpdateCandidacyRequest;
import dev.lucavassos.recruiter.modules.candidacy.entities.Candidacy;
import dev.lucavassos.recruiter.modules.candidacy.entities.CandidacyComment;
import dev.lucavassos.recruiter.modules.candidacy.entities.CandidacyFile;
import dev.lucavassos.recruiter.modules.candidacy.entities.CandidacyId;
import dev.lucavassos.recruiter.modules.candidacy.repository.CandidacyCommentRepository;
import dev.lucavassos.recruiter.modules.candidacy.repository.CandidacyFileRepository;
import dev.lucavassos.recruiter.modules.candidacy.repository.dto.CandidacyCommentDto;
import dev.lucavassos.recruiter.modules.candidacy.repository.dto.CandidacyCommentDtoMapper;
import dev.lucavassos.recruiter.modules.candidacy.repository.dto.CandidacyDto;
import dev.lucavassos.recruiter.modules.candidacy.repository.dto.CandidacyDtoMapper;
import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import dev.lucavassos.recruiter.modules.candidate.repository.CandidateRepository;
import dev.lucavassos.recruiter.modules.candidacy.repository.CandidacyRepository;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.job.entities.JobHistory;
import dev.lucavassos.recruiter.modules.job.repository.JobRepository;
import dev.lucavassos.recruiter.modules.user.domain.RoleName;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import dev.lucavassos.recruiter.monitoring.MonitoringProcessor;
import dev.lucavassos.recruiter.service.fileupload.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class CandidacyService {

    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private CandidacyFileRepository candidacyFileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private CandidacyRepository candidacyRepository;
    @Autowired
    private CandidacyDtoMapper candidacyDtoMapper;
    @Autowired
    private CandidacyCommentRepository candidacyCommentRepository;
    @Autowired
    private CandidacyCommentDtoMapper candidacyCommentDtoMapper;
    @Autowired
    MonitoringProcessor monitoringProcessor;
    @Autowired
    FileUploadService fileUploadService;

    @Transactional
    public void addCandidacy(NewCandidacyRequest candidacy) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();


        // find recruiter
        User recruiter = userRepository.findOneById(userPrincipal.getId()).orElseThrow(
                () -> {
                    log.error("Recruiter with ID {} not found", userPrincipal.getId());
                    return new ResourceNotFoundException("Recruiter not found");
                }
        );
        // find job
        Job job = jobRepository.findOneById(candidacy.jobId()).orElseThrow(
                () -> {
                    log.error("Job with ID {} not found", candidacy.jobId());
                    return new ResourceNotFoundException("Job not found");
                }
        );
        // find candidate
        Candidate candidate = candidateRepository.findOneByPan(candidacy.candidatePan()).orElseThrow(
                () -> {
                    log.error("Candidate with Pan {} not found", candidacy.candidatePan());
                    return new ResourceNotFoundException("Candidate not found");
                }
        );

        if (candidacyRepository.existsByJobAndCandidate(job, candidate)) {
            log.error("Candidacy already exists for job {} and candidate {}", job, candidate);
            throw new RequestValidationException("Candidacy already exists");
        }

        if (candidacy.resume() != null) validateFile(candidacy.resume());

        CandidacyId candidacyId = new CandidacyId(candidate.getPan(), job.getId());
        Candidacy newCandidacy = saveCandidacy(
                Candidacy.builder()
                        .id(candidacyId)
                        .job(job)
                        .candidate(candidate)
                        .recruiter(recruiter)
                        .relevantExperience(candidacy.relevantExperience())
                        .expectedCtc(candidacy.expectedCtc())
                        .officialNoticePeriod(candidacy.officialNoticePeriod())
                        .actualNoticePeriod(candidacy.actualNoticePeriod())
                        .reasonForQuickJoin(candidacy.reasonForQuickJoin())
                        .status(candidacy.status() != null ? candidacy.status() : CandidacyStatus.SENT_TO_CLIENT)
                        .build()
        );
        monitoringProcessor.incrementCandidaciesCounter();

        if ( !StringUtils.isBlank(candidacy.recruiterComment()) ) {
            saveCandidacyComment(CandidacyComment.builder()
                    .text(candidacy.recruiterComment())
                    .candidacy(newCandidacy)
                    .author(recruiter)
                    .build());
        }

        if ( candidacy.resume() != null ) {
            UUID uniqueId = UUID.randomUUID();
            try {
                fileUploadService.uploadResume(candidacy.resume().getInputStream(),
                        candidacy.resume().getName(),
                        uniqueId,
                        candidate.getPan());
                CandidacyFile file = CandidacyFile.builder()
                        .type(candidacy.resume().getContentType())
                        .name(candidacy.resume().getName())
                        .uniqueId(uniqueId)
                        .candidacy(newCandidacy)
                        .build();
                candidacyFileRepository.save(file);
            } catch (IOException ioe) {
                log.error("Error while uploading resume: {}", ioe.getMessage());
                throw new ServerException("Error while uploading resume");
            } catch (Exception e) {
                log.error("Error while saving candidacy file: {}", e.getMessage());
                throw new DatabaseException("Error while saving candidacy file");
            }
        }

    }

    @Transactional
    public CandidacyDto getCandidacy(Long jobId, String pan) {

        Job job = jobRepository.findOneById(jobId).orElseThrow(
                () -> {
                    log.error("Job with id {} not found", jobId);
                    return new ResourceNotFoundException("Job not found");
                }
        );
        Candidate candidate = candidateRepository.findOneByPan(pan).orElseThrow(
                () -> {
                    log.error("Candidate with pan {} not found", pan);
                    return new ResourceNotFoundException("Candidate not found");
                }
        );

        return candidacyRepository.findByJobAndCandidate(job, candidate)
                .map(candidacy ->
                        candidacyDtoMapper.apply(candidacy)
        ).orElseThrow(
                () -> {
                    log.error("Candidacy with job {} and candidate {} not found", job, candidate);
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
    public CandidacyDto updateCandidacy(Long jobId, String pan, UpdateCandidacyRequest request) {

        boolean changes = false;
        log.info("Updating candidacy with jobId {} and pan {}", jobId, pan);

        Job job = jobRepository.findOneById(jobId).orElseThrow(
                () -> {
                    log.error("Job with id [{}] not found.", jobId);
                    return new ResourceNotFoundException(
                            "Job not found.");
                }
        );

        Candidate candidate = candidateRepository.findOneByPan(pan).orElseThrow(
                () -> {
                    log.error("Candidate with pan [{}] not found.", pan);
                    return new ResourceNotFoundException(
                            "Candidate not found.");
                }
        );

        Candidacy candidacy = candidacyRepository.findByJobAndCandidate(job, candidate).orElseThrow(
                () -> {
                    log.error("Candidacy with job ID {} and candidate pan {} not found.", jobId, pan);
                    return new ResourceNotFoundException(
                            "Candidacy not found.");
                }
        );

        User user = getAuthUser();
        if (!isUserAuthorized(user, candidacy)) {
            log.error("User with id {} is not authorized to modify this candidacy", user.getId());
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

        if (request.status() != null && request.status() != candidacy.getStatus()) {
            candidacy.setStatus(request.status());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No updates were made to data.");
        }

        Candidacy updatedCandidacy = saveCandidacy(candidacy);
        log.info("Candidacy updated: [{}]", updatedCandidacy);

        return candidacyDtoMapper.apply(updatedCandidacy);
    }

    @Transactional
    public void addCandidacyComment(Long jobId, String pan, NewCandidacyCommentRequest comment) {
        Job job = jobRepository.findOneById(jobId).orElseThrow(
                () -> {
                    log.error("Job with id {} not found", jobId);
                    return new ResourceNotFoundException("Job not found");
                }
        );
        Candidate candidate = candidateRepository.findOneByPan(pan).orElseThrow(
                () -> {
                    log.error("Candidate with pan {} not found", pan);
                    return new ResourceNotFoundException("Candidate not found");
                }
        );

        Candidacy candidacy = candidacyRepository.findByJobAndCandidate(job, candidate)
                .orElseThrow(
                        () -> {
                            log.error("Candidacy with job {} and candidate {} not found", job, candidate);
                            return new ResourceNotFoundException("Candidacy not found");
                        }
                );

        User user = getAuthUser();
        if (!isUserAuthorized(user, candidacy)) {
            log.error("User with id {} is not authorized to add comments to this candidacy", user.getId());
            throw new UnauthorizedException("Recruiter is unauthorized to add comments to this candidacy");
        }

        try {
            candidacyCommentRepository.save(
                    CandidacyComment.builder()
                            .text(comment.text())
                            .candidacy(candidacy)
                            .author(user)
                            .build()
            );
        } catch (Exception e) {
            log.error("Database error while adding comment to candidacy: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public List<CandidacyCommentDto> getCandidacyComments(Long jobId, String pan) {

        Job job = jobRepository.findOneById(jobId).orElseThrow(
                () -> {
                    log.error("Job with id {} not found", jobId);
                    return new ResourceNotFoundException("Job not found");
                }
        );
        Candidate candidate = candidateRepository.findOneByPan(pan).orElseThrow(
                () -> {
                    log.error("Candidate with pan {} not found", pan);
                    return new ResourceNotFoundException("Candidate not found");
                }
        );

        Candidacy candidacy = candidacyRepository.findByJobAndCandidate(job, candidate)
                .orElseThrow(
                        () -> {
                            log.error("Candidacy with job {} and candidate {} not found", job, candidate);
                            return new ResourceNotFoundException("Candidacy not found");
                        }
                );

        return candidacyCommentRepository.findByCandidacy(candidacy)
                .stream()
                .map(candidacyCommentDtoMapper)
                .toList();
    }

    @Transactional
    public void deleteCandidacy(Long jobId, String pan) {
        Job job = jobRepository.findOneById(jobId).orElseThrow(
                () -> {
                    log.error("Job with id {} not found", jobId);
                    return new ResourceNotFoundException("Job not found");
                }
        );
        Candidate candidate = candidateRepository.findOneByPan(pan).orElseThrow(
                () -> {
                    log.error("Candidate with pan {} not found", pan);
                    return new ResourceNotFoundException("Candidate not found");
                }
        );

        Candidacy candidacy = candidacyRepository.findByJobAndCandidate(job, candidate)
                .orElseThrow(
                        () -> {
                            log.error("Candidacy with job {} and candidate {} not found", job, candidate);
                            return new ResourceNotFoundException("Candidacy not found");
                        }
                );

        User user = getAuthUser();
        if (!isUserAuthorized(user, candidacy)) {
            log.error("User with id {} is not authorized to delete this candidacy", user.getId());
            throw new UnauthorizedException("Recruiter is unauthorized to delete this candidacy");
        }

        try {
            candidacyRepository.delete(candidacy);
        } catch (Exception e) {
            log.error("Database error while deleting candidacy: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    private CandidacyComment saveCandidacyComment(CandidacyComment comment) {
        try {
            return candidacyCommentRepository.save(comment);
        } catch (Exception e) {
            log.error("Database error while adding comment to candidacy: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    private Candidacy saveCandidacy(Candidacy candidacy) {
        try {
            return candidacyRepository.save(candidacy);
        } catch (Exception e) {
            log.error("Database error while updating candidacy: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }

    private User getAuthUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userRepository.findOneById(userPrincipal.getId()).orElseThrow(
                () -> {
                    log.error("User with id {} not found", userPrincipal.getId());
                    return new ResourceNotFoundException("User not found");
                }
        );
    }

    private boolean isUserAuthorized(User recruiter, Candidacy candidacy) {
        return recruiter.getRoleName() == RoleName.ROLE_ADMIN ||
                candidacy.getRecruiter().getId().equals(recruiter.getId());
    }

    private void validateFile(MultipartFile file) {
        if (file.getSize() > 5242880) {
            log.error("File size exceeds the limit of 5MB");
            throw new RequestValidationException("File size exceeds the limit of 5MB");
        }

        if (!Objects.equals(file.getContentType(), "application/pdf")) {
            log.error("File type not allowed: {}", file.getContentType());
            throw new RequestValidationException("File type is not allowed. Only PDF files are accepted.");
        }
    }
}
