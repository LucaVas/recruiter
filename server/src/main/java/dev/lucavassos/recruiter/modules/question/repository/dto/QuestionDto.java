package dev.lucavassos.recruiter.modules.question.repository.dto;

import dev.lucavassos.recruiter.modules.question.domain.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;


public record QuestionDto(
        Long id,

        @NotBlank(message = "Question text is required")
        @Length(max = 255, message = "Question text must be less than 255 characters")
        String text,

        @Length(max = 255, message = "Question answer must be less than 500 characters")
        String answer,

        @NotNull(message = "Question type is required")
        QuestionType questionType
) {}
