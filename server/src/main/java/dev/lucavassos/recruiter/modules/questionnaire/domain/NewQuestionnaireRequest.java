package dev.lucavassos.recruiter.modules.questionnaire.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record NewQuestionnaireRequest(
        @NotBlank(message = "Questionnaire title is required")
        String title,

        @NotBlank(message = "Client name is required")
        String clientName,

        @Valid
        List<NewQuestion> questions
){}
