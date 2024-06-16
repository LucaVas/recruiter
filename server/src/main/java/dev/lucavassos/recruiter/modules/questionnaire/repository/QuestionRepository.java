package dev.lucavassos.recruiter.modules.questionnaire.repository;

import dev.lucavassos.recruiter.modules.questionnaire.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByTextAndQuestionnaireName(String text, String questionnaireName);
}
