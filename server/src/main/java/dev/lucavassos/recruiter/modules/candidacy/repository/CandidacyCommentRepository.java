package dev.lucavassos.recruiter.modules.candidacy.repository;

import dev.lucavassos.recruiter.modules.candidacy.entities.CandidacyComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidacyCommentRepository extends JpaRepository<CandidacyComment, Long> {
}
