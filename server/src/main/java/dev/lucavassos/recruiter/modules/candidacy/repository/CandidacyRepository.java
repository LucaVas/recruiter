package dev.lucavassos.recruiter.modules.candidacy.repository;

import dev.lucavassos.recruiter.modules.candidacy.entities.Candidacy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidacyRepository extends JpaRepository<Candidacy, Long> {
    Optional<Candidacy> findOneById(Long id);
}
