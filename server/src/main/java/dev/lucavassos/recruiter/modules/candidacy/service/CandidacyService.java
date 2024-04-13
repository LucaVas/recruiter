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
import dev.lucavassos.recruiter.modules.candidacy.repository.dto.CandidacyDto;
import dev.lucavassos.recruiter.modules.candidacy.repository.dto.CandidacyDtoMapper;
import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import dev.lucavassos.recruiter.modules.candidate.repository.CandidateRepository;
import dev.lucavassos.recruiter.modules.candidacy.repository.CandidacyRepository;
import dev.lucavassos.recruiter.modules.candidate.repository.dto.CandidateDto;
import dev.lucavassos.recruiter.modules.job.domain.JobResponse;
import dev.lucavassos.recruiter.modules.job.domain.UpdateJobRequest;
import dev.lucavassos.recruiter.modules.job.entities.ContractType;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.job.repository.JobRepository;
import dev.lucavassos.recruiter.modules.skill.entities.Skill;
import dev.lucavassos.recruiter.modules.skill.repository.dto.RawSkillDto;
import dev.lucavassos.recruiter.modules.user.entities.RoleName;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import dev.lucavassos.recruiter.utils.Constants;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        Candidate candidate = candidateRepository.findOneById(candidacy.candidateId()).orElseThrow(
                () -> {
                    LOG.error("Candidate with ID {} not found", candidacy.candidateId());
                    return new ResourceNotFoundException("Candidate not found");
                }
        );

        // save all into a new candidacy
        Candidacy newCandidacy = Candidacy.builder()
                .job(job)
                .recruiter(recruiter)
                .candidate(candidate)
                .relevantExperience(candidacy.relevantExperience())
                .expectedCtc(candidacy.expectedCtc())
                .officialNoticePeriod(candidacy.officialNoticePeriod())
                .actualNoticePeriod(candidacy.actualNoticePeriod())
                .reasonForQuickJoin(candidacy.reasonForQuickJoin())
                .remarks(candidacy.remarks())
                .build();

        candidacyRepository.save(newCandidacy);

    }

    @Transactional
    public CandidacyResponse getCandidacy(Long id) {
        return candidacyRepository.findOneById(id)
                .map(candidacy ->
                        new CandidacyResponse(candidacy.getId(), candidacyDtoMapper.apply(candidacy))
        ).orElseThrow(
                () -> {
                    LOG.error("Candidacy with ID {} not found", id);
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
    public CandidacyResponse updateCandidacy(UpdateCandidacyRequest request) throws Exception {

        boolean changes = false;
        Long id = request.id();
        LOG.info("Updating candidacy with id {}", id);

        Candidacy candidacy = candidacyRepository.findOneById(id).orElseThrow(
                () -> {
                    LOG.error("Candidacy with id [{}] not found.", id);
                    throw new ResourceNotFoundException(
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

        candidacy.setModifiedAt(LocalDateTime.now(Constants.UTC_OFFSET));

        try {
            candidacyRepository.save(candidacy);
        } catch (Exception e) {
            LOG.error("Database error while updating candidacy: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }

        LOG.info("Candidacy updated: [{}]", candidacy);

        return new CandidacyResponse(
                candidacy.getId(),
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
