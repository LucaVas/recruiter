package dev.lucavassos.recruiter.modules.skill.domain;

import jakarta.validation.constraints.NotBlank;

public record NewSkillRequest(
        @NotBlank(message = "Skill name is required")
        String name
){}
