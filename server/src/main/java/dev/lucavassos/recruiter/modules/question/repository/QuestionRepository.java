package dev.lucavassos.recruiter.modules.question.repository;

import dev.lucavassos.recruiter.modules.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
