package dev.lucavassos.recruiter.modules.candidate.service;

import dev.lucavassos.recruiter.exception.DuplicateResourceException;
import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.modules.candidate.domain.CandidateResponse;
import dev.lucavassos.recruiter.modules.candidate.domain.CandidateStatus;
import dev.lucavassos.recruiter.modules.candidate.domain.NewCandidateRequest;
import dev.lucavassos.recruiter.modules.candidate.domain.UpdateCandidateRequest;
import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import dev.lucavassos.recruiter.modules.candidate.repository.CandidateRepository;
import dev.lucavassos.recruiter.modules.candidate.repository.dto.CandidateDto;
import dev.lucavassos.recruiter.modules.candidate.repository.dto.CandidateDtoMapper;
import dev.lucavassos.recruiter.exception.RequestValidationException;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {

    private static final Logger LOG = LoggerFactory.getLogger(CandidateService.class);

    @Autowired
    private CandidateRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CandidateDtoMapper dtoMapper;

    public CandidateResponse addCandidate(NewCandidateRequest request) throws Exception {
        LOG.info("Adding new candidate");
        String email = request.getEmail();
        String phone = request.getPhone();
        String pan = request.getPan();
        Long recuiterId = request.getRecruiterId();

        if (repository.existsCandidateByEmail(email)) {
                throw new DuplicateResourceException(
                        "Candidate with email [%s] already exists.".formatted(email)
                );
        }
        if (repository.existsCandidateByPhone(phone)) {
                throw new DuplicateResourceException(
                        "Candidate with phone [%s] already exists.".formatted(phone)
                );
            }
        if (repository.existsCandidateByPan(pan)) {
            throw new DuplicateResourceException(
                    "Candidate with PAN [%s] already exists.".formatted(pan)
            );
        }
        
        User recruiter = userRepository.findOneById(recuiterId).orElseThrow(
                () -> new ResourceNotFoundException(
                "Recruiter with id [%d] already exists.".formatted(recuiterId)
                )
        );

        Candidate createdCandidate;
            try {
                createdCandidate = repository.save(
                        Candidate.builder()
                                .name(request.getName())
                                .email(email)
                                .phone(phone)
                                .totalExperience(request.getTotalExperience())
                                .relevantExperience(request.getRelevantExperience())
                                .education(request.getEducation())
                                .currentCtc(request.getCurrentCtC())
                                .expectedCtc(request.getExpectedCtC())
                                .officialNoticePeriod(request.getOfficialNoticePeriod())
                                .actualNoticePeriod(request.getActualNoticePeriod())
                                .reasonForQuickJoin(request.getReasonForQuickJoin())
                                .remarks(request.getRemarks())
                                .recruiter(recruiter)
                                .pan(pan)
                                .comments(request.getComments())
                                .build()
                        );
            } catch (Exception e) {
                throw new Exception(e.getCause());
            }


            LOG.info("New candidate created: [{}]", createdCandidate);

            return new CandidateResponse(
                    createdCandidate.getId(),
                    dtoMapper.apply(createdCandidate)
            );

    }

    public CandidateResponse updateCandidate(UpdateCandidateRequest request) throws Exception {

        boolean changes = false;
        Long id = request.getId();
        LOG.info("Updating candidate with id {}", id);

        Candidate candidate = repository.findOneById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Candidate with id [%d] not found.".formatted(id)
                )
        );

        if (request.getName() != null && !request.getName().equals(candidate.getName())) {
            candidate.setName(request.getName());
            changes = true;
        }
        if (request.getEmail() != null && !request.getEmail().equals(candidate.getEmail())) {
            if (repository.existsCandidateByEmail(request.getEmail())) {
                throw new DuplicateResourceException(
                        "Candidate with email [%s] already exists.".formatted(request.getEmail())
                );
            }
            candidate.setEmail(request.getEmail());
            changes = true;
        }
        if (request.getPhone() != null && !request.getPhone().equals(candidate.getPhone())) {
            if (repository.existsCandidateByPhone(request.getPhone())) {
                throw new DuplicateResourceException(
                        "Candidate with phone [%s] already exists.".formatted(request.getPhone())
                );
            }
            candidate.setPhone(request.getPhone());
            changes = true;
        }
        if (request.getTotalExperience() != null && !request.getTotalExperience().equals(candidate.getTotalExperience())) {
            candidate.setTotalExperience(request.getTotalExperience());
            changes = true;
        }
        if (request.getRelevantExperience() != null && !request.getRelevantExperience().equals(candidate.getRelevantExperience())) {
            candidate.setRelevantExperience(request.getRelevantExperience());
            changes = true;
        }
        if (request.getEducation() != null && !request.getEducation().equals(candidate.getEducation())) {
            candidate.setEducation(request.getEducation());
            changes = true;
        }
        if (request.getCurrentCtC() != null && !request.getCurrentCtC().equals(candidate.getCurrentCtc())) {
            candidate.setCurrentCtc(request.getCurrentCtC());
            changes = true;
        }
        if (request.getExpectedCtC() != null && !request.getExpectedCtC().equals(candidate.getExpectedCtc())) {
            candidate.setExpectedCtc(request.getExpectedCtC());
            changes = true;
        }
        if (request.getOfficialNoticePeriod() != null && !request.getOfficialNoticePeriod().equals(candidate.getOfficialNoticePeriod())) {
            candidate.setOfficialNoticePeriod(request.getOfficialNoticePeriod());
            changes = true;
        }
        if (request.getActualNoticePeriod() != null && !request.getActualNoticePeriod().equals(candidate.getActualNoticePeriod())) {
            candidate.setActualNoticePeriod(request.getActualNoticePeriod());
            changes = true;
        }
        if (request.getReasonForQuickJoin() != null && !request.getReasonForQuickJoin().equals(candidate.getReasonForQuickJoin())) {
            candidate.setReasonForQuickJoin(request.getReasonForQuickJoin());
            changes = true;
        }
        if (request.getRemarks() != null && !request.getRemarks().equals(candidate.getRemarks())) {
            candidate.setRemarks(request.getRemarks());
            changes = true;
        }
        if (request.getRecruiterId() != null && !request.getRecruiterId().equals(candidate.getRecruiter().getId())) {
            User recruiter = userRepository
                    .findOneById(request.getRecruiterId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Recruiter with id [%d] already exists.".formatted(request.getRecruiterId())
                    ));
            candidate.setRecruiter(recruiter);
        }
        if (request.getPan() != null && !request.getPan().equals(candidate.getPan())) {
            if (repository.existsCandidateByPan(request.getPan())) {
                throw new DuplicateResourceException(
                        "Candidate with PAN [%s] already exists.".formatted(request.getPan())
                );
            }
            candidate.setPan(request.getPan());
            changes = true;
        }
        if (request.getComments() != null && !request.getComments().equals(candidate.getComments())) {
            candidate.setComments(request.getComments());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No updates were made to data.");
        }

        try {
            repository.save(candidate);
        } catch (Exception e) {
            throw new Exception(e.getCause());
        }


        LOG.info("Candidate updated: [{}]", candidate);

        return new CandidateResponse(
                candidate.getId(),
                dtoMapper.apply(candidate)
        );

    }

    public CandidateDto getCandidateById(Long id) {
        Candidate candidate = repository.findOneById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Candidate with id [%d] not found".formatted(id)
                )
        );

        return dtoMapper.apply(candidate);
    }

    public void changeCandidateStatus(Long id) {
        Candidate candidate = repository.findOneById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Candidate with id [%d] not found".formatted(id)
                )
        );

        switch (candidate.getStatus()) {
            case ACTIVE -> candidate.setStatus(CandidateStatus.ARCHIVED);
            case ARCHIVED -> candidate.setStatus(CandidateStatus.ACTIVE);
        }

        repository.save(candidate);
    }

    public List<CandidateDto> getAllActiveCandidates() {
        List<Candidate> candidates = repository.findAll();

        return candidates
                .stream()
                .filter(candidate -> candidate.getStatus() == CandidateStatus.ACTIVE)
                .map(candidate -> dtoMapper.apply(candidate))
                .toList();
    }
}
