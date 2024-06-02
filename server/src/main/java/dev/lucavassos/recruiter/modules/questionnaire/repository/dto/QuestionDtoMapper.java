package dev.lucavassos.recruiter.modules.questionnaire.repository.dto;

import dev.lucavassos.recruiter.modules.questionnaire.entity.Question;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class QuestionDtoMapper implements Function<Question, QuestionDto> {
    @Override
    public QuestionDto apply(Question question) {
        return new QuestionDto(
                question.getId(),
                question.getText(),
                question.getAnswer(),
                question.getQuestionType()
        );
    }
}
