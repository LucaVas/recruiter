package dev.lucavassos.recruiter.modules.questionnaire.repository;

import dev.lucavassos.recruiter.modules.questionnaire.entity.QuestionHistory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestionHistoryRepository extends JpaRepository<QuestionHistory, Long> {
}
