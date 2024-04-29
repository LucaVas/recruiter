package dev.lucavassos.recruiter.modules.question;

import dev.lucavassos.recruiter.modules.client.repository.ClientRepository;
import dev.lucavassos.recruiter.modules.question.entity.Question;
import dev.lucavassos.recruiter.modules.question.repository.QuestionRepository;
import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDto;
import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDtoMapper;
import dev.lucavassos.recruiter.modules.skill.repository.SkillRepository;
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
    public List<QuestionDto> getQuestionsByClientOrSkill(String clientOrSkill) {
        LOG.info("Retrieving questions for client / skill {}", clientOrSkill);

//        Client client = clientRepository.findByName(clientOrSkill);
//        Skill skill = skillRepository.findByName(skillName)
//                .orElseThrow(() -> new ResourceNotFoundException("Skill not found: " + skillName));

        List<Question> questions = questionRepository.findByClientNameOrSkillName(clientOrSkill);

        List<QuestionDto> questionDtos = questions.stream()
                .map(questionDtoMapper)
                .toList();

        LOG.info("{} questions retrieved: {}", questionDtos.size(), questionDtos);

        return questionDtos;
    }

}
