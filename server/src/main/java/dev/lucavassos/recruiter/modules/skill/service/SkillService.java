package dev.lucavassos.recruiter.modules.skill.service;

import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDtoMapper;
import dev.lucavassos.recruiter.modules.skill.repository.SkillRepository;
import dev.lucavassos.recruiter.modules.skill.repository.dto.RawSkillDto;
import dev.lucavassos.recruiter.modules.skill.repository.dto.RawSkillDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SkillService {

    private static final Logger LOG = LoggerFactory.getLogger(SkillService.class);

    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private RawSkillDtoMapper rawSkillDtoMapper;
    @Autowired
    private QuestionDtoMapper questionDtoMapper;

    @Transactional
    public List<RawSkillDto> getAllSkills() {
        LOG.info("Retrieving {} skills", 1000);

        List<RawSkillDto> skills =
                skillRepository.findAll()
                        .stream()
                        .map(skill -> rawSkillDtoMapper.apply(skill)
                        )
                        .toList();

        LOG.info("Skills retrieved: {} ({})", skills, skills.size());

        return skills;
    }
}
