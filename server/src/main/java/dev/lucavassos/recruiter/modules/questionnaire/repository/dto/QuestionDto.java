package dev.lucavassos.recruiter.modules.questionnaire.repository.dto;

import dev.lucavassos.recruiter.modules.questionnaire.domain.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;


@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public final class QuestionDto {
    private final Long id;

    @NotBlank(message = "Question text is required")
    @Length(max = 255, message = "Question text must be less than 255 characters")

    private final String text;

    @Length(max = 255, message = "Question answer must be less than 500 characters")
    private final String answer;

    @NotNull(message = "Question questionType is required")
    private final QuestionType questionType;
}
