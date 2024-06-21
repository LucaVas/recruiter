package dev.lucavassos.recruiter.modules.candidacy.repository;

import dev.lucavassos.recruiter.modules.candidacy.entities.Candidacy;
import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CandidacyRepository extends JpaRepository<Candidacy, Long> {
    Boolean existsByJobAndCandidate(Job job, Candidate candidate);

    Optional<Candidacy> findByJobAndCandidate(Job job, Candidate candidate);

    List<Candidacy> findByJob(Job job);

    @Query("SELECT c FROM Candidacy c LEFT JOIN FETCH c.comments WHERE c.id = :id")
    Optional<Candidacy> findByIdWithComments(Long id);
}
