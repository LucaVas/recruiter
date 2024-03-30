package dev.lucavassos.recruiter.modules.candidate.repository;

import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    boolean existsCandidateByEmail(String email);
    boolean existsCandidateByPhone(String phone);
    boolean existsCandidateByPan(String pan);
    Optional<Candidate> findOneById(Long id);
}
