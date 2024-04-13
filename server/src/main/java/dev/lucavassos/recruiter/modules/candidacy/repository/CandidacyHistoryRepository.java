package dev.lucavassos.recruiter.modules.candidacy.repository;

import dev.lucavassos.recruiter.modules.candidacy.entities.CandidacyHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidacyHistoryRepository extends JpaRepository<CandidacyHistory, Long> {

}
