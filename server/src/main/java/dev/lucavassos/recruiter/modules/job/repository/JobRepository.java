package dev.lucavassos.recruiter.modules.job.repository;

import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<List<Job>> findByName(String name, Pageable pageable);

    Optional<Job> findOneById(Long id);

    List<Job> findAllByStatusNot(JobStatus status, Pageable pageable);

    Optional<Job> findOneByIdAndStatusNot(Long id, JobStatus status);

}
