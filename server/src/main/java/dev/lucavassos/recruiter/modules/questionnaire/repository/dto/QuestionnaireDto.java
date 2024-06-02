package dev.lucavassos.recruiter.modules.questionnaire.repository.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record QuestionnaireDto(

        String title,

        String clientName,

        @Valid @NotEmpty(message = "Questionnaire must have at least one question")
        Set<QuestionDto> questions
) {}
