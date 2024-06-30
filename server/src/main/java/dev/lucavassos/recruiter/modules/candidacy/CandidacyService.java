package dev.lucavassos.recruiter.modules.candidacy;

import dev.lucavassos.recruiter.exception.DatabaseException;
import dev.lucavassos.recruiter.exception.RequestValidationException;
import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.exception.ServerException;
import dev.lucavassos.recruiter.modules.HistoryEventType;
import dev.lucavassos.recruiter.modules.candidacy.domain.*;
import dev.lucavassos.recruiter.modules.candidacy.entities.*;
import dev.lucavassos.recruiter.modules.candidacy.repository.*;
import dev.lucavassos.recruiter.modules.candidacy.repository.dto.*;
import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import dev.lucavassos.recruiter.modules.candidate.repository.CandidateRepository;
import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.job.repository.JobRepository;
import dev.lucavassos.recruiter.modules.user.domain.RoleName;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import dev.lucavassos.recruiter.service.storage.CandidacyFilesHandler;
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
    private final CandidacyFilesHandler candidacyFilesHandler;
    private final CandidacyHistoryRepository historyRepository;
    private final CandidacyFileHistoryRepository fileHistoryRepository;

    @Transactional
    public void addCandidacy(NewCandidacyRequest candidacy) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User userPrincipal = (User) authentication.getPrincipal();

        User recruiter = userRepository
                .findOneById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
        Job job = jobRepository
                .findByIdAndStatusNotWithClientAndSkillsAndQuestionnaire(candidacy.jobId(), JobStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        Candidate candidate = candidateRepository
                .findOneByPan(candidacy.candidatePan())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        if (candidacyRepository.existsByJobAndCandidate(job, candidate))
            throw new RequestValidationException("Candidacy already exists");

        if (candidacy.files() != null && !candidacy.files().isEmpty())
            candidacy.files().forEach(this::validateFile);

        Candidacy newCandidacy = Candidacy.builder()
                .job(job)
                .candidate(candidate)
                .recruiter(recruiter)
                .relevantExperience(candidacy.relevantExperience())
                .expectedCtc(candidacy.expectedCtc())
                .officialNoticePeriod(candidacy.officialNoticePeriod())
                .actualNoticePeriod(candidacy.actualNoticePeriod())
                .reasonForQuickJoin(candidacy.reasonForQuickJoin())
                .status(candidacy.status() != null ? candidacy.status() : CandidacyStatus.SENT_TO_CLIENT)
                .build();


        Candidacy savedCandidacy = saveCandidacy(newCandidacy);

        if (!StringUtils.isBlank(candidacy.recruiterComment())) {
            CandidacyComment comment = CandidacyComment.builder()
                    .text(candidacy.recruiterComment())
                    .author(recruiter)
                    .candidacy(savedCandidacy)
                    .build();

            candidacyCommentRepository.save(comment);
            savedCandidacy.addComment(comment);
        }

        log.info("saved {}", savedCandidacy);

        saveCandidacyHistoryEvent(recruiter, savedCandidacy, HistoryEventType.CREATED);

        if (candidacy.files() != null && !candidacy.files().isEmpty()) {
            candidacy.files().forEach(file -> {
                UUID uniqueId = UUID.randomUUID();
                try {
                    candidacyFilesHandler.upload(file.getInputStream(),
                            candidate.getPan(),
                            job.getId(),
                            file.getOriginalFilename());
                    CandidacyFile newFile = CandidacyFile.builder()
                            .type(file.getContentType())
                            .name(file.getOriginalFilename())
                            .uniqueId(uniqueId)
                            .candidacy(newCandidacy)
                            .build();
                    candidacyFileRepository.save(newFile);
                    saveCandidacyFileHistoryEvent(recruiter, newFile, HistoryEventType.CREATED);
                } catch (IOException ioe) {
                    throw new ServerException("Error while uploading candidacy file");
                } catch (Exception e) {
                    throw new DatabaseException("Error while saving candidacy file");
                }
            });
        }

    }

    @Transactional
    public CandidacyDto getCandidacy(Long id) {
        Candidacy candidacy = findCandidacy(id);
        return candidacyDtoMapper.apply(candidacy);
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
    public CandidacyDto updateCandidacy(Long id, UpdateCandidacyRequest request) {

        boolean changes = false;
        log.debug("Updating candidacy with id {}", id);

        Candidacy candidacy = findCandidacy(id);

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
            throw new RequestValidationException("No changes were made to the candidacy.");
        }

        Candidacy updatedCandidacy = saveCandidacy(candidacy);
        log.debug("Candidacy updated: [{}]", updatedCandidacy);

        saveCandidacyHistoryEvent(user, updatedCandidacy, HistoryEventType.UPDATED);
        return candidacyDtoMapper.apply(updatedCandidacy);
    }

    @Transactional
    public void addCandidacyComment(Long id, NewCandidacyCommentRequest comment) {

        Candidacy candidacy = findCandidacy(id);
        User user = getAuthUser();
        if (!isUserAuthorized(user, candidacy))
            throw new AccessDeniedException("Recruiter is unauthorized to add comments to this candidacy");

        CandidacyComment newComment = CandidacyComment.builder()
                .text(comment.text())
                .author(user)
                .candidacy(candidacy)
                .build();
        candidacy.addComment(newComment);

        candidacyCommentRepository.save(newComment);
        candidacyRepository.save(candidacy);
    }

    @Transactional
    public List<CandidacyCommentDto> getCandidacyComments(Long id) {

        Candidacy candidacy = candidacyRepository
                .findByIdWithComments(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidacy not found"));
        log.info("candidacy {}", candidacy);
        return candidacy.getComments().stream()
                .map(candidacyCommentDtoMapper)
                .toList();
    }

    @Transactional
    public List<CandidacyFileDto> getCandidacyFiles(Long id) {

        Candidacy candidacy = findCandidacy(id);
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
            candidacyFilesHandler.delete(candidacy.getCandidate().getPan(), candidacy.getJob().getId(), file.getName());
        } catch (Exception e) {
            throw new ServerException("Error while deleting candidacy file");
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

        return candidacyFilesHandler.get(candidacy.getCandidate().getPan(), candidacy.getJob().getId(), file.getName());
    }

    @Transactional
    public void deleteCandidacy(Long id) {

        Candidacy candidacy = findCandidacy(id);
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
    public void changeCandidacyStatus(Long id, ChangeCandidacyStatusRequest request) {
        Candidacy candidacy = findCandidacy(id);

        User user = getAuthUser();
        if (!candidacy.getStatus().equals(request.status())) {
            candidacy.setStatus(request.status());
        }
        saveCandidacy(candidacy);
        saveCandidacyHistoryEvent(user, candidacy, HistoryEventType.UPDATED);
    }

    @Transactional
    public void addFilesToCandidacy(Long id, List<MultipartFile> files) {

        Candidacy candidacy = findCandidacy(id);
        User user = getAuthUser();
        if (!isUserAuthorized(user, candidacy))
            throw new AccessDeniedException("Recruiter is unauthorized to upload files to this candidacy");

        if (files == null || files.isEmpty())
            throw new RequestValidationException("No files were provided to upload");

        files.forEach(file -> {
            validateFile(file);
            UUID uniqueId = UUID.randomUUID();
            try {
                candidacyFilesHandler.upload(file.getInputStream(),
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
                saveCandidacyFileHistoryEvent(user, newFile, HistoryEventType.CREATED);
            } catch (IOException ioe) {
                throw new ServerException("Error while uploading candidacy file");
            } catch (Exception e) {
                throw new DatabaseException("Error while saving candidacy file");
            }
        });

    }

    @Transactional
    private Candidacy saveCandidacy(Candidacy candidacy) {
        try {
            return candidacyRepository.save(candidacy);
        } catch (Exception e) {
            log.error("Database error while saving candidacy: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    private void saveCandidacyHistoryEvent(User user, Candidacy candidacy, HistoryEventType eventType) {
        try {
            CandidacyHistory event = CandidacyHistory.builder()
                    .relevantExperience(candidacy.getRelevantExperience())
                    .expectedCtc(candidacy.getExpectedCtc())
                    .officialNoticePeriod(candidacy.getOfficialNoticePeriod())
                    .actualNoticePeriod(candidacy.getActualNoticePeriod())
                    .reasonForQuickJoin(candidacy.getReasonForQuickJoin())
                    .status(candidacy.getStatus())
                    .eventType(eventType)
                    .candidacy(candidacy)
                    .modifiedBy(user)
                    .build();
            historyRepository.save(event);
        } catch (Exception e) {
            log.error("Database error while saving candidacy history event: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    private void saveCandidacyFileHistoryEvent(User user, CandidacyFile candidacyFile, HistoryEventType eventType) {
        try {
            CandidacyFileHistory event = CandidacyFileHistory.builder()
                    .type(candidacyFile.getType())
                    .name(candidacyFile.getName())
                    .eventId(candidacyFile.getUniqueId())
                    .eventType(eventType)
                    .candidacyFile(candidacyFile)
                    .modifiedBy(user)
                    .build();
            fileHistoryRepository.save(event);
        } catch (Exception e) {
            log.error("Database error while saving candidacy file history event: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
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
    private Candidacy findCandidacy(Long id) {
        try {
            return candidacyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Candidacy not found"));
        } catch (Exception e) {
            log.error("Database error while finding candidacy: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
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
