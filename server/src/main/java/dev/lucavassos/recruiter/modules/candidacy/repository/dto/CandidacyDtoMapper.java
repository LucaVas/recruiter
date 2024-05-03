package dev.lucavassos.recruiter.modules.candidacy.repository.dto;

import dev.lucavassos.recruiter.modules.candidacy.entities.Candidacy;
import dev.lucavassos.recruiter.modules.candidate.repository.dto.CandidateDtoMapper;
import dev.lucavassos.recruiter.modules.job.repository.dto.JobDtoMapper;
import dev.lucavassos.recruiter.modules.user.repository.dto.RecruiterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CandidacyDtoMapper implements Function<Candidacy, CandidacyDto> {

    @Autowired
    private JobDtoMapper jobDtoMapper;

    @Autowired
    private CandidateDtoMapper candidateDtoMapper;

    @Override
    public CandidacyDto apply(Candidacy candidacy) {
        return new CandidacyDto(
                jobDtoMapper.apply(candidacy.getJob()),
                new RecruiterDto(
                        candidacy.getRecruiter().getId(),
                        candidacy.getRecruiter().getUsername()
                ),
                candidateDtoMapper.apply(candidacy.getCandidate()),
                candidacy.getRelevantExperience(),
                candidacy.getExpectedCtc(),
                candidacy.getOfficialNoticePeriod(),
                candidacy.getActualNoticePeriod(),
                candidacy.getReasonForQuickJoin(),
                candidacy.getRemarks(),
                candidacy.getStatus(),
                candidacy.getCreatedDTime(),
                candidacy.getModifiedDTime()
        );
    }
}
