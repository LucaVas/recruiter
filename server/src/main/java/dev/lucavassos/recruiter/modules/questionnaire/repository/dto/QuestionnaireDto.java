package dev.lucavassos.recruiter.modules.questionnaire.repository.dto;

import dev.lucavassos.recruiter.modules.client.entities.Client;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record QuestionnaireDto(
        String title,
        Client client,
        @Valid @NotEmpty(message = "Questionnaire must have at least one question")
        Set<QuestionDto> questions
) {}
