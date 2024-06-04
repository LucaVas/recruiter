package dev.lucavassos.recruiter.modules.candidacy.repository;

import dev.lucavassos.recruiter.modules.candidacy.entities.Candidacy;
import dev.lucavassos.recruiter.modules.candidacy.entities.CandidacyFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidacyFileRepository extends JpaRepository<CandidacyFile, Long> {
    List<CandidacyFile> findByCandidacy(Candidacy candidacy);
}
