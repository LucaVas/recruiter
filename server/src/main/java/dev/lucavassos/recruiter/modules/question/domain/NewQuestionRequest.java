package dev.lucavassos.recruiter.modules.question.domain;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

import java.util.List;

public record NewQuestionRequest(
        @NotBlank(message = "Question text is required")
        String text,

        @Nullable
        String division,

        @NotBlank(message = "Question title is required")
        String title,

        @NotNull(message = "Question answer is required")
        @Size(max = 500, message = "Question answer must be less than 500 characters")
        String answer,

        @NotNull(message = "Client ID is required")
        Long clientId,

        @Nullable
        List<String> skillNames
){}
