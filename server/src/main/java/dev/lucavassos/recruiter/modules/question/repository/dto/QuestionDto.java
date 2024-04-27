package dev.lucavassos.recruiter.modules.question.repository.dto;

import java.time.LocalDateTime;

public record QuestionDto(
        Long id,
        String text,
        Boolean active,
        LocalDateTime createdDTime,
        LocalDateTime modifiedDTime
) {
}
