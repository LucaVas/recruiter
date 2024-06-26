package dev.lucavassos.recruiter.modules.candidate.repository;

import dev.lucavassos.recruiter.modules.candidate.entities.CandidateHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateHistoryRepository extends JpaRepository<CandidateHistory, Long> {

}
