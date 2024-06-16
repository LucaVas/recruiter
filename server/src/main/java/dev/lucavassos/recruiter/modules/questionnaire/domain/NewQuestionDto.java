package dev.lucavassos.recruiter.modules.questionnaire.domain;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewQuestionDto(

        Long id,

        @NotBlank(message = "Question text is required")
        @Size(max = 255, message = "Question text cannot be longer than 255 characters")
        String text,

        @Size(max = 500, message = "Question answer cannot be longer than 500 characters")
        String answer,

        @NotNull(message = "Question type is required")
        QuestionType questionType
) {}
