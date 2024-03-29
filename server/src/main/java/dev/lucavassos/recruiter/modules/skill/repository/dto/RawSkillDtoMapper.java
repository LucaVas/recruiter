package dev.lucavassos.recruiter.modules.skill.repository.dto;

import dev.lucavassos.recruiter.modules.skill.entities.Skill;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class RawSkillDtoMapper implements Function<Skill, RawSkillDto> {
    @Override
    public RawSkillDto apply(Skill skill) {
        return new RawSkillDto(
                skill.getId(),
                skill.getName()
        );
    }
}
