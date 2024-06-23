package dev.lucavassos.recruiter.modules.questionnaire.repository;

import dev.lucavassos.recruiter.modules.questionnaire.entity.QuestionnaireHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionnaireHistoryRepository extends JpaRepository<QuestionnaireHistory, Long> {
}

