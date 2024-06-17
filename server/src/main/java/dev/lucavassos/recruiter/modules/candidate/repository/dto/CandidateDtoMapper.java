package dev.lucavassos.recruiter.modules.candidate.repository.dto;

import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CandidateDtoMapper implements Function<Candidate, CandidateDto> {
    @Override
    public CandidateDto apply(Candidate candidate) {
        return CandidateDto.builder()
                .pan(candidate.getPan())
                .name(candidate.getName())
                .phone(candidate.getPhone())
                .email(candidate.getEmail())
                .totalExperience(candidate.getTotalExperience())
                .education(candidate.getEducation())
                .currentCtc(candidate.getCurrentCtc())
                .status(candidate.getStatus())
                .createdAt(candidate.getCreatedAt())
                .updatedAt(candidate.getUpdatedAt())
                .build();
    }
}
