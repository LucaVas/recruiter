package dev.lucavassos.recruiter.modules.candidate.repository.dto;

import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CandidateDtoMapper implements Function<Candidate, CandidateDto> {
    @Override
    public CandidateDto apply(Candidate candidate) {
        return new CandidateDto(
                candidate.getName(),
                candidate.getPhone(),
                candidate.getEmail(),
                candidate.getTotalExperience(),
                candidate.getEducation(),
                candidate.getCurrentCtc(),
                candidate.getPan(),
                candidate.getCreatedDTime()
        );
    }
}
