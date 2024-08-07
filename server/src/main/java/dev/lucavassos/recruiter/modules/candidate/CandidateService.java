package dev.lucavassos.recruiter.modules.candidate;

import dev.lucavassos.recruiter.exception.*;
import dev.lucavassos.recruiter.modules.HistoryEventType;
import dev.lucavassos.recruiter.modules.candidate.domain.CandidateResponse;
import dev.lucavassos.recruiter.modules.candidate.domain.CandidateStatus;
import dev.lucavassos.recruiter.modules.candidate.domain.NewCandidateRequest;
import dev.lucavassos.recruiter.modules.candidate.domain.UpdateCandidateRequest;
import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import dev.lucavassos.recruiter.modules.candidate.entities.CandidateHistory;
import dev.lucavassos.recruiter.modules.candidate.repository.CandidateHistoryRepository;
import dev.lucavassos.recruiter.modules.candidate.repository.CandidateRepository;
import dev.lucavassos.recruiter.modules.candidate.repository.dto.CandidateDto;
import dev.lucavassos.recruiter.modules.candidate.repository.dto.CandidateDtoMapper;
import dev.lucavassos.recruiter.modules.user.domain.RoleName;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandidateService {

    private static final Logger LOG = LoggerFactory.getLogger(CandidateService.class);
    private final CandidateRepository candidateRepository;
    private final CandidateHistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final CandidateDtoMapper dtoMapper;

    @Transactional
    public CandidateResponse addCandidate(NewCandidateRequest request) {
        LOG.info("Adding a new candidate");

        String pan = request.pan();
        String email = request.email();
        String phone = request.phone();

        // check if candidate exists
        if (candidateRepository.existsCandidateByPan(pan)) {
            throw new DuplicateResourceException(
                    "Candidate with pan [%s] already exists.".formatted(pan)
            );
        }
        if (candidateRepository.existsCandidateByEmail(email)) {
            throw new DuplicateResourceException(
                    "Candidate with email [%s] already exists.".formatted(email)
            );
        }
        if (candidateRepository.existsCandidateByPhone(phone)) {
            throw new DuplicateResourceException(
                    "Candidate with phone [%s] already exists.".formatted(phone)
            );
        }

        User recruiter = getAuthUser();
        Candidate newCandidate;
        try {
            newCandidate = candidateRepository.save(
                    Candidate.builder()
                            .name(request.name())
                            .phone(request.phone())
                            .email(request.email())
                            .totalExperience(request.totalExperience())
                            .education(request.education())
                            .currentCtc(request.currentCtc())
                            .pan(request.pan())
                            .status(CandidateStatus.ACTIVE)
                            .recruiter(recruiter)
                            .build()
            );
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }

        LOG.info("New candidate created: [{}]", newCandidate);

        saveCandidateHistoryEvent(recruiter, newCandidate, HistoryEventType.CREATED);
        return new CandidateResponse(
                newCandidate.getPan(),
                dtoMapper.apply(newCandidate)
        );
    }

    @Transactional
    public CandidateResponse updateCandidate(String pan, UpdateCandidateRequest request) throws Exception {

        boolean changes = false;
        LOG.info("Updating candidate with pan {}", pan);

        Candidate candidate = this.candidateRepository.findOneByPan(pan).orElseThrow(
                () -> {
                    LOG.error("Candidate with pan {} not found", pan);
                    return new ResourceNotFoundException("Candidate not found.");
                }
        );

        User user = getAuthUser();
        if (!isUserAuthorized(user, candidate)) {
            LOG.error("User with id {} is not authorized to modify this candidate", user.getId());
            throw new AccessDeniedException("Recruiter is unauthorized to modify this candidate");
        }

        if (request.name() != null && !request.name().equals(candidate.getName())) {
            candidate.setName(request.name());
            changes = true;
        }
        if (request.email() != null && !request.email().equals(candidate.getEmail())) {
            if (this.candidateRepository.existsCandidateByEmail(request.email()))
                throw new DuplicateResourceException("Email already taken.");

            candidate.setEmail(request.email());
            changes = true;
        }
        if (request.phone() != null && !request.phone().equals(candidate.getPhone())) {
            if (this.candidateRepository.existsCandidateByPhone(request.phone()))
                throw new DuplicateResourceException("Phone already taken.");

            candidate.setPhone(request.phone());
            changes = true;
        }
        if (request.totalExperience() != null && !request.totalExperience().equals(candidate.getTotalExperience())) {
            candidate.setTotalExperience(request.totalExperience());
            changes = true;
        }
        if (request.currentCtc() != null && !request.currentCtc().equals(candidate.getCurrentCtc())) {
            candidate.setCurrentCtc(request.currentCtc());
            changes = true;
        }
        if (request.education() != null && !request.education().equals(candidate.getEducation())) {
            candidate.setEducation(request.education());
            changes = true;
        }


        if (!changes) {
            throw new RequestValidationException("No updates were made to the candidate.");
        }

        try {
            this.candidateRepository.save(candidate);
        } catch (Exception e) {
            throw new Exception(e.getCause());
        }

        LOG.info("Candidate updated: [{}]", candidate);
        saveCandidateHistoryEvent(user, candidate, HistoryEventType.UPDATED);

        return new CandidateResponse(
                candidate.getPan(),
                dtoMapper.apply(candidate)
        );

    }

    @Transactional
    public CandidateResponse findCandidate(String panOrEmailOrPhone) {

        // look for pan
        Candidate candidate = this.candidateRepository.findOneByPanOrPhoneOrEmail(panOrEmailOrPhone)
                .orElseThrow(
                        () -> {
                            LOG.info("Candidate with identifier {} not found", panOrEmailOrPhone);
                            return new ResourceNotFoundException("Candidate not found");
                        });

        return new CandidateResponse(
                candidate.getPan(),
                dtoMapper.apply(candidate)
        );
    }

    @Transactional
    public void changeCandidateStatus(String pan) {
        Candidate candidate = this.candidateRepository.findOneByPan(pan).orElseThrow(
                () -> {
                    LOG.error("Candidate with pan {} not found", pan);
                    return new ResourceNotFoundException("Candidate not found");
                }
        );
        User user = getAuthUser();

        switch (candidate.getStatus()) {
            case ACTIVE -> candidate.setStatus(CandidateStatus.ARCHIVED);
            case ARCHIVED -> candidate.setStatus(CandidateStatus.ACTIVE);
        }

        candidateRepository.save(candidate);
        saveCandidateHistoryEvent(user, candidate, HistoryEventType.UPDATED);
    }

    @Transactional
    public List<CandidateDto> getAllCandidates() {

        User user = getAuthUser();

        List<Candidate> candidates = candidateRepository.findAll();

        return candidates
                .stream()
                .filter(candidate -> user.isAdmin() || candidate.getRecruiter().getId().equals(user.getId()))
                .map(dtoMapper)
                .toList();
    }

    private User getAuthUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User userPrincipal = (User) authentication.getPrincipal();
        return userRepository.findOneById(userPrincipal.getId()).orElseThrow(
                () -> {
                    LOG.error("User with id {} not found", userPrincipal.getId());
                    return new ResourceNotFoundException("User not found");
                }
        );
    }

    private boolean isUserAuthorized(User recruiter, Candidate candidate) {
        return recruiter.getRoleName() == RoleName.ADMIN
                || candidate.getRecruiter().getId().equals(recruiter.getId());
    }

    @Transactional
    private void saveCandidateHistoryEvent(User user, Candidate candidate, HistoryEventType eventType) {
        try {
            CandidateHistory event = CandidateHistory.builder()
                    .pan(candidate.getPan())
                    .name(candidate.getName())
                    .phone(candidate.getPhone())
                    .email(candidate.getEmail())
                    .totalExperience(candidate.getTotalExperience())
                    .education(candidate.getEducation())
                    .currentCtc(candidate.getCurrentCtc())
                    .status(candidate.getStatus())
                    .eventType(eventType)
                    .candidate(candidate)
                    .modifiedBy(user)
                    .build();
            historyRepository.save(event);
        } catch (Exception e) {
            log.error("Database error while saving candidate history event: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }
}
