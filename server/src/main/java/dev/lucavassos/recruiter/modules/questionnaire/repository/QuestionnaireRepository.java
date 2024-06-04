package dev.lucavassos.recruiter.modules.questionnaire.repository;

import dev.lucavassos.recruiter.modules.questionnaire.entity.Questionnaire;
import dev.lucavassos.recruiter.modules.questionnaire.entity.QuestionnaireId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionnaireRepository extends JpaRepository<Questionnaire, QuestionnaireId> {

    List<Questionnaire> findByIdTitleOrIdClientName(String title, String clientName, Pageable pageable);

    Optional<Questionnaire> findByIdTitleAndIdClientName(String title, String clientName);

    Boolean existsByIdClientNameAndIdTitle(String clientName, String title);
}

