package dev.lucavassos.recruiter.modules.job.repository.dto;

import dev.lucavassos.recruiter.modules.job.entities.JobHistory;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class JobHistoryDtoMapper implements Function<JobHistory, JobHistoryDto> {
    @Override
    public JobHistoryDto apply(JobHistory JobHistory) {
        return new JobHistoryDto(
                JobHistory.getId(),
                JobHistory.getStatus(),
                JobHistory.getBonusPayPerCv(),
                JobHistory.getClosureBonus(),
                JobHistory.getCreatedAt(),
                JobHistory.getModifiedAt()
        );
    }
}
