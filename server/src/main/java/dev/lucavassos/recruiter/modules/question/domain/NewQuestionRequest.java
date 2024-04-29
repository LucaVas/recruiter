package dev.lucavassos.recruiter.modules.question.domain;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

public record NewQuestionRequest(
        @NotNull(message = "Question text cannot be empty") String text,

        @Nullable String division,

        @NotNull(message = "Question answer cannot be empty")
        @Size(max = 255, message = "Question answer must be less than 500 characters")
        String answer,

        @NotNull(message = "Client ID cannot be empty")
        Long clientId,

        @NotNull(message = "Client ID cannot be empty")
        Long skillId
){}
