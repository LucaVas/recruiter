package dev.lucavassos.recruiter.modules.questionnaire.repository.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

public record QuestionnaireDto(

        Long id,

        @NotBlank(message = "Questionnaire title is required")
        @Length(max = 255, message = "Questionnaire title must be less than 255 characters")
        String title,

        @Valid @NotEmpty(message = "Questionnaire must have at least one question")
        Set<QuestionDto> questions
) {}
