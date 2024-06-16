package dev.lucavassos.recruiter.modules.job.repository;

import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("SELECT j FROM Job j " +
            "LEFT JOIN FETCH j.client " +
            "LEFT JOIN FETCH j.skills " +
            "LEFT JOIN FETCH j.questionnaire q " +
            "LEFT JOIN FETCH q.questions " +
            "WHERE j.id = :id AND j.status != :status")
    Optional<Job> findByIdAndStatusNotWithClientAndSkillsAndQuestionnaire(@Param("id") Long id, @Param("status") JobStatus status);

    List<Job> findByStatusNot(JobStatus status, Pageable pageable);
}
