package dev.lucavassos.recruiter.modules.question.repository;

import dev.lucavassos.recruiter.modules.question.entity.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {

    @Query("SELECT q FROM Questionnaire q " +
            "LEFT JOIN q.client c " +
            "WHERE UPPER(q.title) = UPPER(:titleOrClient) " +
            "OR UPPER(c.name) = UPPER(:titleOrClient) ")
    List<Questionnaire> findByTitleOrClient(String titleOrClient);

    Optional<Questionnaire> findByTitle(String title);
}

