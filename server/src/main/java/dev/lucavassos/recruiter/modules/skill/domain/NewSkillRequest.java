package dev.lucavassos.recruiter.modules.skill.domain;

import jakarta.validation.constraints.NotNull;

public record NewSkillRequest(
        @NotNull(message = "Skill name cannot be empty") String name
){}
