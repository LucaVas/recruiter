package dev.lucavassos.recruiter.modules.question.repository;

import dev.lucavassos.recruiter.modules.client.entities.Client;
import dev.lucavassos.recruiter.modules.question.entity.Question;
import dev.lucavassos.recruiter.modules.skill.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT q FROM Question q WHERE q.clients_id = ?1 AND q.skills_id = ?2")
    List<Question> findByClientAndSkill(Long clientId, Long skillId);
}
