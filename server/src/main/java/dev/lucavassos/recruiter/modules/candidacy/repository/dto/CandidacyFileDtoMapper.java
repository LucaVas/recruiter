package dev.lucavassos.recruiter.modules.candidacy.repository.dto;

import dev.lucavassos.recruiter.modules.candidacy.entities.CandidacyFile;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CandidacyFileDtoMapper implements Function<CandidacyFile, CandidacyFileDto> {

    @Override
    public CandidacyFileDto apply(CandidacyFile file) {
        return new CandidacyFileDto(
                file.getId(),
                file.getType(),
                file.getName(),
                file.getUniqueId(),
                file.getCreatedAt(),
                file.getUpdatedAt()
        );
    }
}
