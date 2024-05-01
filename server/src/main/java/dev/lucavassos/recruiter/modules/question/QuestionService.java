package dev.lucavassos.recruiter.modules.question;

import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.exception.ServerException;
import dev.lucavassos.recruiter.modules.candidate.domain.CandidateStatus;
import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import dev.lucavassos.recruiter.modules.client.entities.Client;
import dev.lucavassos.recruiter.modules.client.repository.ClientRepository;
import dev.lucavassos.recruiter.modules.question.domain.NewQuestionRequest;
import dev.lucavassos.recruiter.modules.question.entity.Question;
import dev.lucavassos.recruiter.modules.question.repository.QuestionRepository;
import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDto;
import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDtoMapper;
import dev.lucavassos.recruiter.modules.skill.entities.Skill;
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
    public List<QuestionDto> getQuestionsByTitleOrClientOrSkill(String titleOrClientOrSkill) {
        LOG.info("Retrieving questions for title / client / skill {}", titleOrClientOrSkill);

        List<Question> questions = questionRepository.findByTitleOrClientOrSkill(titleOrClientOrSkill);

        List<QuestionDto> questionDtos = questions.stream()
                .map(questionDtoMapper)
                .toList();

        LOG.info("{} questions retrieved: {}", questionDtos.size(), questionDtos);

        return questionDtos;
    }

    @Transactional
    public QuestionDto createQuestion(NewQuestionRequest request) {
        LOG.info("Creating new question");

        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        Skill skill = null;
        if (request.skillId() != null) {
            skill = skillRepository.findById(request.skillId())
                    .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
        }

        Question question;
        try {
            question = Question.builder()
                    .title(request.title())
                    .text(request.text())
                    .answer(request.answer())
                    .client(client)
                    .skill(skill)
                    .active(true)
                    .build();
            questionRepository.save(question);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }

        QuestionDto questionDto = questionDtoMapper.apply(question);

        LOG.info("Question created: {}", questionDto);

        return questionDto;
    }

}
