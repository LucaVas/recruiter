package dev.lucavassos.recruiter.modules.questionnaire.repository;

import dev.lucavassos.recruiter.modules.questionnaire.entity.Questionnaire;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {

    List<Questionnaire> findByTitleOrClientName(String title, String clientName, Pageable pageable);

    Optional<Questionnaire> findByTitleAndClientName(String title, String clientName);

    Optional<Questionnaire> findByTitle(String title);

    Boolean existsByClientNameAndTitle(String clientName, String title);

    @Query("SELECT q FROM Questionnaire q LEFT JOIN FETCH p.question WHERE p.id = :id")
    Optional<Questionnaire> findByIdWithQuestions(@Param("id") Long id);
}

