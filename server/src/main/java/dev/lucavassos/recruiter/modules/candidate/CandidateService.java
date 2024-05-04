package dev.lucavassos.recruiter.modules.candidate;

import dev.lucavassos.recruiter.auth.UserPrincipal;
import dev.lucavassos.recruiter.exception.*;
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
import dev.lucavassos.recruiter.monitoring.MonitoringProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CandidateService {

    private static final Logger LOG = LoggerFactory.getLogger(CandidateService.class);

    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private CandidateHistoryRepository historyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CandidateDtoMapper dtoMapper;
    @Autowired
    MonitoringProcessor monitoringProcessor;

    public CandidateResponse addCandidate(NewCandidateRequest request) throws Exception {
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
        monitoringProcessor.incrementCandidatesCounter();

        return new CandidateResponse(
                newCandidate.getPan(),
                dtoMapper.apply(newCandidate)
        );
    }

    public CandidateResponse updateCandidate(UpdateCandidateRequest request) throws Exception {

        boolean changes = false;
        String pan = request.pan();
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
            throw new UnauthorizedException("Recruiter is unauthorized to modify this candidate");
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
            saveCandidateInHistoryTable(candidate, user);
        } catch (Exception e) {
            throw new Exception(e.getCause());
        }

        LOG.info("Candidate updated: [{}]", candidate);

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

    public void changeCandidateStatus(String pan) {
        Candidate candidate = this.candidateRepository.findOneByPan(pan).orElseThrow(
                () -> {
                    LOG.error("Candidate with pan {} not found", pan);
                    return new ResourceNotFoundException("Candidate not found");
                }
        );

        switch (candidate.getStatus()) {
            case ACTIVE -> candidate.setStatus(CandidateStatus.ARCHIVED);
            case ARCHIVED -> candidate.setStatus(CandidateStatus.ACTIVE);
        }

        this.candidateRepository.save(candidate);
    }

    public List<CandidateDto> getAllCandidates() {

        User user = getAuthUser();

        List<Candidate> candidates = candidateRepository.findAll();

        return candidates
                .stream()
                .filter(candidate -> user.isAdmin() || candidate.getRecruiter().getId().equals(user.getId()))
                .map(candidate -> dtoMapper.apply(candidate))
                .toList();
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

    private boolean isUserAuthorized(User recruiter, Candidate candidate) {
        return recruiter.getRoleName() == RoleName.ROLE_ADMIN
               || candidate.getRecruiter().getId().equals(recruiter.getId());
    }

    private void saveCandidateInHistoryTable(Candidate candidate, User user) {
        try {
            // Create new entry in history table
            historyRepository.save(
                    CandidateHistory.builder()
                            .name(candidate.getName())
                            .phone(candidate.getPhone())
                            .email(candidate.getEmail())
                            .totalExperience(candidate.getTotalExperience())
                            .education(candidate.getEducation())
                            .currentCtc(candidate.getCurrentCtc())
                            .status(candidate.getStatus())
                            .candidate(candidate)
                            .modifiedBy(user)
                            .build()
            );
        } catch (Exception e) {
            LOG.error("Error while saving candidate in history table: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }
}
