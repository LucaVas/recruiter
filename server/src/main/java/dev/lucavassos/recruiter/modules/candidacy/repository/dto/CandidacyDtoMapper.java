package dev.lucavassos.recruiter.modules.candidacy.repository.dto;

import dev.lucavassos.recruiter.modules.candidacy.entities.Candidacy;
import dev.lucavassos.recruiter.modules.candidate.repository.dto.CandidateDtoMapper;
import dev.lucavassos.recruiter.modules.job.repository.dto.JobDTOMapper;
import dev.lucavassos.recruiter.modules.user.repository.dto.RecruiterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CandidacyDtoMapper implements Function<Candidacy, CandidacyDto> {

    @Autowired
    private JobDTOMapper jobDtoMapper;

    @Autowired
    private CandidateDtoMapper candidateDtoMapper;

    @Override
    public CandidacyDto apply(Candidacy candidacy) {
        return CandidacyDto.builder()
                .job(jobDtoMapper.apply(candidacy.getJob()))
                .recruiter(new RecruiterDto(
                        candidacy.getRecruiter().getId(),
                        candidacy.getRecruiter().getName()
                ))
                .candidate(candidateDtoMapper.apply(candidacy.getCandidate()))
                .relevantExperience(candidacy.getRelevantExperience())
                .expectedCtc(candidacy.getExpectedCtc())
                .officialNoticePeriod(candidacy.getOfficialNoticePeriod())
                .actualNoticePeriod(candidacy.getActualNoticePeriod())
                .reasonForQuickJoin(candidacy.getReasonForQuickJoin())
                .status(candidacy.getStatus())
                .createdAt(candidacy.getCreatedAt())
                .updatedAt(candidacy.getUpdatedAt())
                .build();
    }
}
