package dev.lucavassos.recruiter.modules.candidate.repository.dto;

import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import dev.lucavassos.recruiter.modules.user.repository.dto.RecruiterDto;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CandidateDtoMapper implements Function<Candidate, CandidateDto> {
    @Override
    public CandidateDto apply(Candidate candidate) {
        return new CandidateDto(
                candidate.getId(),
                candidate.getName(),
                candidate.getPhone(),
                candidate.getEmail(),
                candidate.getTotalExperience(),
                candidate.getRelevantExperience(),
                candidate.getEducation(),
                candidate.getCurrentCtc(),
                candidate.getExpectedCtc(),
                candidate.getOfficialNoticePeriod(),
                candidate.getActualNoticePeriod(),
                candidate.getReasonForQuickJoin(),
                candidate.getRemarks(),
                new RecruiterDto(
                        candidate.getRecruiter().getId(),
                        candidate.getRecruiter().getUsername()
                ),
                candidate.getPan(),
                candidate.getComments(),
                candidate.getStatus(),
                candidate.getPerCvRatePaymentDate(),
                candidate.getClosureBonusPaymentDate()
        );
    }
}
