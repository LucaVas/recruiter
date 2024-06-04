package dev.lucavassos.recruiter.modules.candidacy.repository;

import dev.lucavassos.recruiter.modules.candidacy.entities.Candidacy;
import dev.lucavassos.recruiter.modules.candidacy.entities.CandidacyComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidacyCommentRepository extends JpaRepository<CandidacyComment, Long> {
    List<CandidacyComment> findByCandidacy(Candidacy candidacy);
}
