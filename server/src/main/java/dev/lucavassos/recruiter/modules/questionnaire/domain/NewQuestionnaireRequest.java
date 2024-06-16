package dev.lucavassos.recruiter.modules.questionnaire.domain;

import dev.lucavassos.recruiter.modules.client.repository.dto.ClientDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class NewQuestionnaireRequest {
    @NotBlank(message = "Questionnaire title is required")
    protected String title;

    @Valid
    @NotNull(message = "Questionnaire must have a client")
    ClientDto client;

    @Valid
    @NotEmpty(message = "Questionnaire must have at least one question")
    List<NewQuestionDto> questions;
}
