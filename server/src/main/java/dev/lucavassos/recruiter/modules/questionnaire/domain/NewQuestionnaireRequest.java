package dev.lucavassos.recruiter.modules.questionnaire.domain;

import dev.lucavassos.recruiter.modules.client.entities.Client;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class NewQuestionnaireRequest {
        @NotBlank(message = "Questionnaire title is required")
        protected String title;

        @NotBlank(message = "Client is required")
        Client client;

        @Valid
        @NotEmpty(message = "Questionnaire must have at least one question")
        List<NewQuestionDto> questions;
}
