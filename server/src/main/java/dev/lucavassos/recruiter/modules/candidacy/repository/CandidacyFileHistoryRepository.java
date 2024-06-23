package dev.lucavassos.recruiter.modules.candidacy.repository;

import dev.lucavassos.recruiter.modules.candidacy.entities.CandidacyFileHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidacyFileHistoryRepository extends JpaRepository<CandidacyFileHistory, Long> {

}
