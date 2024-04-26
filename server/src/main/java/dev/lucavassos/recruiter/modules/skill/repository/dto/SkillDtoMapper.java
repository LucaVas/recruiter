package dev.lucavassos.recruiter.modules.skill.repository.dto;

import dev.lucavassos.recruiter.modules.skill.entities.Skill;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class SkillDtoMapper implements Function<Skill, SkillDto> {
    @Override
    public SkillDto apply(Skill skill) {
        return new SkillDto(
                skill.getId(),
                skill.getName());
    }
}
