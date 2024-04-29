package dev.lucavassos.recruiter.modules.question;

import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.modules.client.entities.Client;
import dev.lucavassos.recruiter.modules.client.repository.ClientRepository;
import dev.lucavassos.recruiter.modules.question.entity.Question;
import dev.lucavassos.recruiter.modules.question.repository.QuestionRepository;
import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDto;
import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDtoMapper;
import dev.lucavassos.recruiter.modules.skill.entities.Skill;
import dev.lucavassos.recruiter.modules.skill.repository.SkillRepository;
import dev.lucavassos.recruiter.modules.skill.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;
    private final ClientRepository clientRepository;
    private final SkillRepository skillRepository;
    private final QuestionDtoMapper questionDtoMapper;

    @Transactional
    public List<QuestionDto> getQuestionsByClientAndSkill(String clientName, String skillName) {
        LOG.info("Retrieving questions for client {} and skill {}", clientName, skillName);

        Client client = clientRepository.findByName(clientName)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found: " + clientName));
        Skill skill = skillRepository.findByName(skillName)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found: " + skillName));

        List<Question> questions = questionRepository.findAll();

        List<QuestionDto> questionDtos = questions.stream()
                .map(questionDtoMapper)
                .toList();

        LOG.info("{} questions retrieved: {}", questionDtos.size(), questionDtos);

        return questionDtos;
    }

}
