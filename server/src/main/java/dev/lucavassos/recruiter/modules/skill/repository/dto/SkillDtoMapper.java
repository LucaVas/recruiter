package dev.lucavassos.recruiter.modules.skill.repository.dto;

import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDto;
import dev.lucavassos.recruiter.modules.skill.entities.Skill;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SkillDtoMapper implements Function<Skill, SkillDto> {
    @Override
    public SkillDto apply(Skill skill) {
        return new SkillDto(
                skill.getId(),
                skill.getName(),
                skill.getQuestions().stream()
                        .map(question -> new QuestionDto(
                                question.getId(),
                                question.getText())
                        )
                        .collect(Collectors.toSet())
        );
    }
}
