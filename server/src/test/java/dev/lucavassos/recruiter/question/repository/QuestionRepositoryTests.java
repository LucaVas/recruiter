package dev.lucavassos.recruiter.question.repository;

import dev.lucavassos.recruiter.modules.questionnaire.entity.Question;
import dev.lucavassos.recruiter.modules.questionnaire.repository.QuestionRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static dev.lucavassos.recruiter.testUtils.RandomUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
public class QuestionRepositoryTests {

    @Autowired
    private QuestionRepository repository;

    @Autowired
    private TestEntityManager manager;


    @Test
    public void save_creates_new_active_question() {

        String questionText = randomString(10, 50);
        Question question = Question.builder()
                .title(randomString(10, 50))
                .text(questionText)
                .active(true)
                .answer(randomString(10, 50))
                .build();

        Question savedQuestion = repository.save(question);

        Question existsQuestion = manager.find(Question.class, savedQuestion.getId());

        assertThat(existsQuestion.getActive()).isEqualTo(true);
        assertThat(existsQuestion.getText()).isEqualTo(questionText);
    }

    @Test
    public void save_throws_error_if_question_text_is_invalid() {
        Question questionWithShortText = Question.builder()
                .title(randomString(10, 50))
                .text("")
                .active(true)
                .answer(randomString(10, 50))
                .build();

        assertThatThrownBy(() -> {
            repository.save(questionWithShortText);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Question text must be at least 1 character long");
    }
}
