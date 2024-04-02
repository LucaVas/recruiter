package dev.lucavassos.recruiter.modules.candidate.service;

import dev.lucavassos.recruiter.exception.DuplicateResourceException;
import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.exception.ServerException;
import dev.lucavassos.recruiter.modules.candidate.domain.NewCandidateResponse;
import dev.lucavassos.recruiter.modules.candidate.domain.CandidateStatus;
import dev.lucavassos.recruiter.modules.candidate.domain.NewCandidateRequest;
import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import dev.lucavassos.recruiter.modules.candidate.repository.CandidateRepository;
import dev.lucavassos.recruiter.modules.candidate.repository.dto.CandidateDto;
import dev.lucavassos.recruiter.modules.candidate.repository.dto.CandidateDtoMapper;
import dev.lucavassos.recruiter.modules.candidate.repository.dto.NewCandidateDtoMapper;
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
    private CandidateRepository candidateRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CandidateDtoMapper dtoMapper;
    @Autowired
    private NewCandidateDtoMapper newCandidateDtoMapper;

    public NewCandidateResponse addCandidate(NewCandidateRequest request) throws Exception {
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

        // add a new candidate
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
                                .build()
                        );
            } catch (Exception e) {
                throw new ServerException(e.getMessage());
            }

            LOG.info("New candidate created: [{}]", newCandidate);

            return new NewCandidateResponse(
                    newCandidate.getId(),
                    newCandidateDtoMapper.apply(newCandidate)
            );
    }

//    public CandidateResponse updateCandidate(UpdateCandidateRequest request) throws Exception {
//
//        boolean changes = false;
//        Long id = request.getId();
//        LOG.info("Updating candidate with id {}", id);
//
//        Candidate candidate = this.candidateRepository.findOneById(id).orElseThrow(
//                () -> new ResourceNotFoundException(
//                        "Candidate with id [%d] not found.".formatted(id)
//                )
//        );
//
//        if (request.getName() != null && !request.getName().equals(candidate.getName())) {
//            candidate.setName(request.getName());
//            changes = true;
//        }
//        if (request.getEmail() != null && !request.getEmail().equals(candidate.getEmail())) {
//            if (this.candidateRepository.existsCandidateByEmail(request.getEmail())) {
//                throw new DuplicateResourceException(
//                        "Candidate with email [%s] already exists.".formatted(request.getEmail())
//                );
//            }
//            candidate.setEmail(request.getEmail());
//            changes = true;
//        }
//        if (request.getPhone() != null && !request.getPhone().equals(candidate.getPhone())) {
//            if (this.candidateRepository.existsCandidateByPhone(request.getPhone())) {
//                throw new DuplicateResourceException(
//                        "Candidate with phone [%s] already exists.".formatted(request.getPhone())
//                );
//            }
//            candidate.setPhone(request.getPhone());
//            changes = true;
//        }
//        if (request.getTotalExperience() != null && !request.getTotalExperience().equals(candidate.getTotalExperience())) {
//            candidate.setTotalExperience(request.getTotalExperience());
//            changes = true;
//        }
//        if (request.getRelevantExperience() != null && !request.getRelevantExperience().equals(candidate.getRelevantExperience())) {
//            candidate.setRelevantExperience(request.getRelevantExperience());
//            changes = true;
//        }
//        if (request.getEducation() != null && !request.getEducation().equals(candidate.getEducation())) {
//            candidate.setEducation(request.getEducation());
//            changes = true;
//        }
//        if (request.getCurrentCtC() != null && !request.getCurrentCtC().equals(candidate.getCurrentCtc())) {
//            candidate.setCurrentCtc(request.getCurrentCtC());
//            changes = true;
//        }
//        if (request.getExpectedCtC() != null && !request.getExpectedCtC().equals(candidate.getExpectedCtc())) {
//            candidate.setExpectedCtc(request.getExpectedCtC());
//            changes = true;
//        }
//        if (request.getOfficialNoticePeriod() != null && !request.getOfficialNoticePeriod().equals(candidate.getOfficialNoticePeriod())) {
//            candidate.setOfficialNoticePeriod(request.getOfficialNoticePeriod());
//            changes = true;
//        }
//        if (request.getActualNoticePeriod() != null && !request.getActualNoticePeriod().equals(candidate.getActualNoticePeriod())) {
//            candidate.setActualNoticePeriod(request.getActualNoticePeriod());
//            changes = true;
//        }
//        if (request.getReasonForQuickJoin() != null && !request.getReasonForQuickJoin().equals(candidate.getReasonForQuickJoin())) {
//            candidate.setReasonForQuickJoin(request.getReasonForQuickJoin());
//            changes = true;
//        }
//        if (request.getRemarks() != null && !request.getRemarks().equals(candidate.getRemarks())) {
//            candidate.setRemarks(request.getRemarks());
//            changes = true;
//        }
//        if (request.getRecruiterId() != null && !request.getRecruiterId().equals(candidate.getRecruiter().getId())) {
//            User recruiter = userRepository
//                    .findOneById(request.getRecruiterId())
//                    .orElseThrow(() -> new ResourceNotFoundException(
//                            "Recruiter with id [%d] already exists.".formatted(request.getRecruiterId())
//                    ));
//            candidate.setRecruiter(recruiter);
//        }
//        if (request.getPan() != null && !request.getPan().equals(candidate.getPan())) {
//            if (this.candidateRepository.existsCandidateByPan(request.getPan())) {
//                throw new DuplicateResourceException(
//                        "Candidate with PAN [%s] already exists.".formatted(request.getPan())
//                );
//            }
//            candidate.setPan(request.getPan());
//            changes = true;
//        }
//        if (request.getComments() != null && !request.getComments().equals(candidate.getComments())) {
//            candidate.setComments(request.getComments());
//            changes = true;
//        }
//
//        if (!changes) {
//            throw new RequestValidationException("No updates were made to data.");
//        }
//
//        try {
//            this.candidateRepository.save(candidate);
//        } catch (Exception e) {
//            throw new Exception(e.getCause());
//        }
//
//
//        LOG.info("Candidate updated: [{}]", candidate);
//
//        return new CandidateResponse(
//                candidate.getId(),
//                dtoMapper.apply(candidate)
//        );
//
//    }

    public CandidateDto findCandidateByPan(String pan) {
        Candidate candidate = this.candidateRepository.findOneByPan(pan).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Candidate with PAN [%s] not found".formatted(pan)
                )
        );

        return dtoMapper.apply(candidate);
    }

    public void changeCandidateStatus(Long id) {
        Candidate candidate = this.candidateRepository.findOneById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Candidate with id [%d] not found".formatted(id)
                )
        );

        switch (candidate.getStatus()) {
            case ACTIVE -> candidate.setStatus(CandidateStatus.ARCHIVED);
            case ARCHIVED -> candidate.setStatus(CandidateStatus.ACTIVE);
        }

        this.candidateRepository.save(candidate);
    }

    public List<CandidateDto> getAllActiveCandidates() {
        List<Candidate> candidates = candidateRepository.findAll();

        return candidates
                .stream()
                .filter(candidate -> candidate.getStatus() == CandidateStatus.ACTIVE)
                .map(candidate -> dtoMapper.apply(candidate))
                .toList();
    }
}
