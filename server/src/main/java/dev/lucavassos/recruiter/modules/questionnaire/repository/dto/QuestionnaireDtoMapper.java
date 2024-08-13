package dev.lucavassos.recruiter.modules.questionnaire.repository.dto;

import dev.lucavassos.recruiter.modules.questionnaire.entity.Questionnaire;
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
        if (questionnaire == null) return null;
        return QuestionnaireDto
                .builder()
                .id(questionnaire.getId())
                .title(questionnaire.getTitle())
                .client(questionnaire.getClient())
                .questions(questionnaire.getQuestions()
                        .stream()
                        .map(questionDtoMapper)
                        .collect(Collectors.toList()))
                .createdAt(questionnaire.getCreatedAt())
                .updatedAt(questionnaire.getUpdatedAt())
                .build();
    }
}
