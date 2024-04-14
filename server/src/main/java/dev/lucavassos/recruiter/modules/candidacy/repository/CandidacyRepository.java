package dev.lucavassos.recruiter.modules.candidacy.repository;

import dev.lucavassos.recruiter.modules.candidacy.entities.Candidacy;
import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidacyRepository extends JpaRepository<Candidacy, Long> {
    Boolean existsByJobAndCandidate(Job job, Candidate candidate);
    Optional<Candidacy> findByJobAndCandidate(Job job, Candidate candidate);
    List<Candidacy> findByJob(Job job);
}
