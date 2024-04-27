package dev.lucavassos.recruiter.modules.skill.repository.dto;

import java.time.LocalDateTime;

public record SkillDto(
        Long id,
        String name,
        LocalDateTime createdDTime,
        LocalDateTime modifiedDTime
) {
}
