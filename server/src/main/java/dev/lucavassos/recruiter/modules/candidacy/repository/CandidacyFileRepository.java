package dev.lucavassos.recruiter.modules.candidacy.repository;

import dev.lucavassos.recruiter.modules.candidacy.entities.CandidacyFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidacyFileRepository extends JpaRepository<CandidacyFile, Long> {

}
