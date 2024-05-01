package dev.lucavassos.recruiter.modules.question.repository;

import dev.lucavassos.recruiter.modules.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT q FROM Question q " +
            "LEFT JOIN q.client c " +
            "LEFT JOIN q.skill s " +
            "WHERE UPPER(q.title) = UPPER(:titleOrClientOrSkill) " +
            "OR UPPER(c.name) = UPPER(:titleOrClientOrSkill) " +
            "OR UPPER(s.name) = UPPER(:titleOrClientOrSkill)")
    List<Question> findByTitleOrClientOrSkill(String titleOrClientOrSkill);
}
