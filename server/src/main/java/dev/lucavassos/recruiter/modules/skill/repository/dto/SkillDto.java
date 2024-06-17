package dev.lucavassos.recruiter.modules.skill.repository.dto;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public final class SkillDto {
    private final Long id;
    private final String name;
}
