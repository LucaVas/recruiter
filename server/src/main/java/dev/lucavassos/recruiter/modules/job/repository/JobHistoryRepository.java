package dev.lucavassos.recruiter.modules.job.repository;

import dev.lucavassos.recruiter.modules.job.entities.JobHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobHistoryRepository extends JpaRepository<JobHistory, Long> {

}
