package dev.lucavassos.recruiter.modules.questionnaire.domain;

import dev.lucavassos.recruiter.modules.questionnaire.repository.dto.QuestionDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UpdateQuestionnaireRequest {
    @NotBlank(message = "Questionnaire title is required")
    protected String title;

    @Valid
    @NotEmpty(message = "Questionnaire must have at least one question")
    List<QuestionDto> questions;
}
