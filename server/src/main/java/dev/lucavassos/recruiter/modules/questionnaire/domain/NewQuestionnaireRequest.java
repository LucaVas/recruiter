package dev.lucavassos.recruiter.modules.questionnaire.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record NewQuestionnaireRequest(
        @NotBlank(message = "Questionnaire title is required")
        String title,

        @Valid
        List<NewQuestion> questions,

        @NotNull(message = "Client ID is required")
        Long clientId
){}
