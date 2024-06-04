package dev.lucavassos.recruiter.modules.candidacy;

import dev.lucavassos.recruiter.exception.*;
import dev.lucavassos.recruiter.modules.candidacy.domain.*;
import dev.lucavassos.recruiter.modules.candidacy.entities.Candidacy;
import dev.lucavassos.recruiter.modules.candidacy.entities.CandidacyComment;
import dev.lucavassos.recruiter.modules.candidacy.entities.CandidacyFile;
import dev.lucavassos.recruiter.modules.candidacy.entities.CandidacyId;
import dev.lucavassos.recruiter.modules.candidacy.repository.CandidacyCommentRepository;
import dev.lucavassos.recruiter.modules.candidacy.repository.CandidacyFileRepository;
import dev.lucavassos.recruiter.modules.candidacy.repository.dto.*;
import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import dev.lucavassos.recruiter.modules.candidate.repository.CandidateRepository;
import dev.lucavassos.recruiter.modules.candidacy.repository.CandidacyRepository;
import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.job.repository.JobRepository;
import dev.lucavassos.recruiter.modules.user.domain.RoleName;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import dev.lucavassos.recruiter.monitoring.MonitoringProcessor;
import dev.lucavassos.recruiter.service.storage.ResumeHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
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
@RequiredArgsConstructor
public class CandidacyService {

    private final CandidateRepository candidateRepository;
    private final CandidacyFileRepository candidacyFileRepository;
    private final CandidacyFileDtoMapper candidacyFileDtoMapper;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final CandidacyRepository candidacyRepository;
    private final CandidacyDtoMapper candidacyDtoMapper;
    private final CandidacyCommentRepository candidacyCommentRepository;
    private final CandidacyCommentDtoMapper candidacyCommentDtoMapper;
    private final MonitoringProcessor monitoringProcessor;
    private final ResumeHandler resumeHandler;

    @Transactional
    public void addCandidacy(NewCandidacyRequest candidacy) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User userPrincipal = (User) authentication.getPrincipal();

        User recruiter = userRepository
                .findOneById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
        Job job = jobRepository
                .findOneByIdAndStatusNot(candidacy.jobId(), JobStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        Candidate candidate = candidateRepository
                .findOneByPan(candidacy.candidatePan())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        if (candidacyRepository.existsByJobAndCandidate(job, candidate))
            throw new RequestValidationException("Candidacy already exists");

        if (candidacy.resume() != null) validateFile(candidacy.resume());

        Candidacy newCandidacy = saveCandidacy(
                Candidacy.builder()
                        .id(new CandidacyId(candidate.getPan(), job.getId()))
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

        if (!StringUtils.isBlank(candidacy.recruiterComment())) {
            saveCandidacyComment(CandidacyComment.builder()
                    .text(candidacy.recruiterComment())
                    .candidacy(newCandidacy)
                    .author(recruiter)
                    .build());
        }

        if (candidacy.resume() != null) {
            UUID uniqueId = UUID.randomUUID();
            try {
                resumeHandler.uploadResume(candidacy.resume().getInputStream(),
                        candidate.getPan(),
                        job.getId(),
                        candidacy.resume().getOriginalFilename());
                CandidacyFile file = CandidacyFile.builder()
                        .type(candidacy.resume().getContentType())
                        .name(candidacy.resume().getOriginalFilename())
                        .uniqueId(uniqueId)
                        .candidacy(newCandidacy)
                        .build();
                candidacyFileRepository.save(file);
            } catch (IOException ioe) {
                throw new ServerException("Error while uploading resume");
            } catch (Exception e) {
                throw new DatabaseException("Error while saving candidacy file");
            }
        }

    }

    @Transactional
    public CandidacyDto getCandidacy(Long jobId, String pan) {
        return candidacyDtoMapper.apply(findIfExist(jobId, pan));
    }

    @Transactional
    public List<CandidacyDto> getAllCandidacies() {
        User user = getAuthUser();
        return candidacyRepository
                .findAll()
                .stream()
                .filter(candidacy -> user.isAdmin() || candidacy.getRecruiter().getId().equals(user.getId()))
                .map(candidacyDtoMapper)
                .toList();
    }

    @Transactional
    public CandidacyDto updateCandidacy(Long jobId, String pan, UpdateCandidacyRequest request) {

        boolean changes = false;
        log.debug("Updating candidacy with jobId {} and pan {}", jobId, pan);

        Candidacy candidacy = findIfExist(jobId, pan);

        User user = getAuthUser();
        if (!isUserAuthorized(user, candidacy))
            throw new AccessDeniedException("Recruiter is unauthorized to modify this candidacy");


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
        log.debug("Candidacy updated: [{}]", updatedCandidacy);

        return candidacyDtoMapper.apply(updatedCandidacy);
    }

    @Transactional
    public void addCandidacyComment(Long jobId, String pan, NewCandidacyCommentRequest comment) {

        Candidacy candidacy = findIfExist(jobId, pan);
        User user = getAuthUser();
        if (!isUserAuthorized(user, candidacy))
            throw new AccessDeniedException("Recruiter is unauthorized to add comments to this candidacy");

        saveCandidacyComment(
                CandidacyComment.builder()
                        .text(comment.text())
                        .candidacy(candidacy)
                        .author(user)
                        .build());
    }

    @Transactional
    public List<CandidacyCommentDto> getCandidacyComments(Long jobId, String pan) {

        Candidacy candidacy = findIfExist(jobId, pan);
        return candidacyCommentRepository
                .findByCandidacy(candidacy)
                .stream()
                .map(candidacyCommentDtoMapper)
                .toList();
    }

    @Transactional
    public List<CandidacyFileDto> getCandidacyFiles(Long jobId, String pan) {

        Candidacy candidacy = findIfExist(jobId, pan);
        return candidacyFileRepository
                .findByCandidacy(candidacy)
                .stream()
                .map(candidacyFileDtoMapper)
                .toList();
    }

    @Transactional
    public void deleteCandidacyFile(Long fileId) {

        CandidacyFile file = candidacyFileRepository
                .findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidacy file not found"));

        Candidacy candidacy = file.getCandidacy();

        User user = getAuthUser();
        if (!isUserAuthorized(user, candidacy))
            throw new AccessDeniedException("Recruiter is unauthorized to delete this file");

        try {
            resumeHandler.deleteResume(candidacy.getCandidate().getPan(), candidacy.getJob().getId(), file.getName());
        } catch (Exception e) {
            throw new ServerException("Error while deleting resume");
        }

        try {
            candidacyFileRepository.delete(file);
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public byte[] getCandidacyFile(Long fileId) {

        CandidacyFile file = candidacyFileRepository
                .findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidacy file not found"));

        Candidacy candidacy = file.getCandidacy();
        User user = getAuthUser();
        if (!isUserAuthorized(user, candidacy))
            throw new AccessDeniedException("Recruiter is unauthorized to get this file");

        return resumeHandler.getResume(candidacy.getCandidate().getPan(), candidacy.getJob().getId(), file.getName());
    }

    @Transactional
    public void deleteCandidacy(Long jobId, String pan) {

        Candidacy candidacy = findIfExist(jobId, pan);
        User user = getAuthUser();
        if (!isUserAuthorized(user, candidacy))
            throw new AccessDeniedException("Recruiter is unauthorized to delete this candidacy");

        try {
            candidacyRepository.delete(candidacy);
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public void addFilesToCandidacy(Long jobId, String pan, List<MultipartFile> files) {

        Candidacy candidacy = findIfExist(jobId, pan);
        User user = getAuthUser();
        if (!isUserAuthorized(user, candidacy))
            throw new AccessDeniedException("Recruiter is unauthorized to upload files to this candidacy");

        if (files == null || files.isEmpty())
            throw new RequestValidationException("No files were provided to upload");

        files.forEach(file -> {
            validateFile(file);
            UUID uniqueId = UUID.randomUUID();
            try {
                resumeHandler.uploadResume(file.getInputStream(),
                        candidacy.getCandidate().getPan(),
                        candidacy.getJob().getId(),
                        file.getOriginalFilename());
                CandidacyFile newFile = CandidacyFile.builder()
                        .type(file.getContentType())
                        .name(file.getOriginalFilename())
                        .uniqueId(uniqueId)
                        .candidacy(candidacy)
                        .build();
                candidacyFileRepository.save(newFile);
            } catch (IOException ioe) {
                throw new ServerException("Error while uploading resume");
            } catch (Exception e) {
                throw new DatabaseException("Error while saving candidacy file");
            }
        });

    }


    @Transactional
    private void saveCandidacyComment(CandidacyComment comment) {
        try {
            candidacyCommentRepository.save(comment);
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
        User userPrincipal = (User) authentication.getPrincipal();
        return userRepository.findOneById(userPrincipal.getId()).orElseThrow(
                () -> {
                    log.error("User with id {} not found", userPrincipal.getId());
                    return new ResourceNotFoundException("User not found");
                }
        );
    }

    @Transactional
    private Candidacy findIfExist(Long jobId, String pan) {
        Job job = jobRepository
                .findOneByIdAndStatusNot(jobId, JobStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        Candidate candidate = candidateRepository
                .findOneByPan(pan)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        return candidacyRepository
                .findByJobAndCandidate(job, candidate)
                .orElseThrow(() -> new ResourceNotFoundException("Candidacy not found"));
    }

    private boolean isUserAuthorized(User recruiter, Candidacy candidacy) {
        return recruiter.getRoleName() == RoleName.ADMIN ||
                candidacy.getRecruiter().getId().equals(recruiter.getId());
    }

    private void validateFile(MultipartFile file) {
        if (file.getSize() > 5242880)
            throw new RequestValidationException("File size exceeds the limit of 5MB");

        if (!Objects.equals(file.getContentType(), "application/pdf"))
            throw new RequestValidationException("File questionType is not allowed. Only PDF files are accepted.");
    }
}
