package dev.lucavassos.recruiter.modules.job.repository.dto;

import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDto;

import java.util.List;

public record SkillDto(
        Long id,
        String name,
        List<QuestionDto> questions
) {
}
