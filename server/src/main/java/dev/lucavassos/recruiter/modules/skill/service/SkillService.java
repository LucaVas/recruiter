package dev.lucavassos.recruiter.modules.skill.service;

import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDtoMapper;
import dev.lucavassos.recruiter.modules.skill.repository.SkillRepository;
import dev.lucavassos.recruiter.modules.skill.repository.dto.RawSkillDto;
import dev.lucavassos.recruiter.modules.skill.repository.dto.RawSkillDtoMapper;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDto;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private SkillDtoMapper skillDtoMapper;
    @Autowired
    private QuestionDtoMapper questionDtoMapper;

    @Transactional
    public List<SkillDto> getAllSkills() {

        Pageable limit = PageRequest.of(0,10);
        LOG.info("Retrieving {} skills", limit.getPageSize());


        List<SkillDto> skills =
                skillRepository.findAll(limit)
                        .stream()
                        .map(skill -> skillDtoMapper.apply(skill)
                        )
                        .toList();

        LOG.info("Skills retrieved: {} ({})", skills, skills.size());

        return skills;
    }
}
