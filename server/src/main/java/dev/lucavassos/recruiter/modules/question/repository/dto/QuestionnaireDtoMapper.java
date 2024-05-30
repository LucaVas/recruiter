package dev.lucavassos.recruiter.modules.question.repository.dto;

import dev.lucavassos.recruiter.modules.question.entity.Questionnaire;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionnaireDtoMapper implements Function<Questionnaire, QuestionnaireDto> {

    private final QuestionDtoMapper questionDtoMapper;
    @Override
    public QuestionnaireDto apply(Questionnaire questionnaire) {
        return new QuestionnaireDto(
                questionnaire.getTitle(),
                questionnaire.getQuestions().stream().map(questionDtoMapper).collect(Collectors.toSet())
        );
    }
}
