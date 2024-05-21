package dev.lucavassos.recruiter.modules.skill;

import dev.lucavassos.recruiter.exception.DuplicateResourceException;
import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDtoMapper;
import dev.lucavassos.recruiter.modules.skill.domain.NewSkillRequest;
import dev.lucavassos.recruiter.modules.skill.entities.Skill;
import dev.lucavassos.recruiter.modules.skill.repository.SkillRepository;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDto;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SkillService {

    private final SkillRepository skillRepository;
    private final SkillDtoMapper skillDtoMapper;
    private final QuestionDtoMapper questionDtoMapper;

    @Transactional
    public List<SkillDto> getAllSkills(Integer pageNumber, Integer pageSize) {

        Pageable limit = PageRequest.of(pageNumber,pageSize);
        log.debug("Retrieving {} skills", limit.getPageSize());


        List<SkillDto> skills =
                skillRepository.findAll(limit)
                        .stream()
                        .map(skillDtoMapper)
                        .toList();

        log.debug("Skills retrieved: {} ({})", skills, skills.size());

        return skills;
    }

    @Transactional
    public SkillDto createSkill (NewSkillRequest request) {
        log.debug("Creating new skill: {}", request);
        if (skillRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Skill with name %s already exists".formatted(request.name()));
        }
        Skill skill = Skill.builder().name(request.name()).build();
        return skillDtoMapper.apply(skillRepository.save(skill));
    }
}
