package dev.lucavassos.recruiter.modules.job.repository.dto;

import dev.lucavassos.recruiter.modules.questionnaire.repository.dto.QuestionnaireDto;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class JobWithQuestionnaireDto extends JobDto {
    @Valid
    private QuestionnaireDto questionnaire;
}
