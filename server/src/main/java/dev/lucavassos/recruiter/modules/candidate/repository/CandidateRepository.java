package dev.lucavassos.recruiter.modules.candidate.repository;

import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    boolean existsCandidateByEmail(String email);
    boolean existsCandidateByPhone(String phone);
    boolean existsCandidateByPan(String pan);
    Optional<Candidate> findOneById(Long id);
    @Query("SELECT c FROM Candidate c WHERE c.pan = :usernameOrEmail OR c.phone = :usernameOrEmail OR c.email = :usernameOrEmail")
    Optional<Candidate> findOneByPanOrPhoneOrEmail(@Param("usernameOrEmail") String usernameOrEmail);
}
