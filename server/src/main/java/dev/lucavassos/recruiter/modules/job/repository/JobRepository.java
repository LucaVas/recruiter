package dev.lucavassos.recruiter.modules.job.repository;

import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.questionnaire.entity.Questionnaire;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<List<Job>> findByName(String name, Pageable pageable);

    Optional<Job> findOneById(Long id);

    List<Job> findAllByStatusNot(JobStatus status, Pageable pageable);

    Optional<Job> findOneByIdAndStatusNot(Long id, JobStatus status);

    @Query("SELECT j FROM Job j LEFT JOIN FETCH j.questionnaire WHERE j.id = :id AND p.status != :status")
    Optional<Questionnaire> findByIdAndStatusNotWithQuestionnaire(@Param("id") Long id, @Param("status") JobStatus status);

}
