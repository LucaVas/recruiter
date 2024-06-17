package dev.lucavassos.recruiter.modules.questionnaire.repository.dto;

import dev.lucavassos.recruiter.modules.client.entities.Client;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class QuestionnaireDto {
    private Long id;
    private String title;
    private Client client;

    @Valid
    @NotEmpty(message = "Questionnaire must have at least one question")
    private List<QuestionDto> questions;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
