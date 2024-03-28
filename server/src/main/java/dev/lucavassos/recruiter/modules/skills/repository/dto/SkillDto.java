package dev.lucavassos.recruiter.modules.skills.repository.dto;

import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDto;

import java.util.Set;

public record SkillDto(
        Long id,
        String name,
        Set<QuestionDto> questions
) {
}
