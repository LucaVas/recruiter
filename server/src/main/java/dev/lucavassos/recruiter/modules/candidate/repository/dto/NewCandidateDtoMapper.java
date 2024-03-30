package dev.lucavassos.recruiter.modules.candidate.repository.dto;

import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class NewCandidateDtoMapper implements Function<Candidate, NewCandidateDto> {
    @Override
    public NewCandidateDto apply(Candidate candidate) {
        return new NewCandidateDto(
                candidate.getId(),
                candidate.getName(),
                candidate.getPhone(),
                candidate.getEmail(),
                candidate.getTotalExperience(),
                candidate.getEducation(),
                candidate.getCurrentCtc(),
                candidate.getPan(),
                candidate.getCreatedAt()
        );
    }
}
