package dev.lucavassos.recruiter.modules.question.repository.dto;

import java.time.LocalDateTime;

public record QuestionDto(
        Long id,
        String text,
        String answer,
        Boolean active,
        String division,
        LocalDateTime createdDTime,
        LocalDateTime modifiedDTime
) {
}
