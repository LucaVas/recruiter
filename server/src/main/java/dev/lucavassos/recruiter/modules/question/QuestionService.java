package dev.lucavassos.recruiter.modules.question;

import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.exception.ServerException;
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
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ClientRepository clientRepository;
    private final SkillRepository skillRepository;
    private final QuestionDtoMapper questionDtoMapper;

    @Transactional
    public List<QuestionDto> getQuestionsByTitleOrClient(String findByTitleOrClient) {
        log.info("Retrieving questions for title / client {}", findByTitleOrClient);

        List<Question> questions = questionRepository.findByTitleOrClient(findByTitleOrClient);

        List<QuestionDto> questionDtos = questions.stream()
                .map(questionDtoMapper)
                .toList();

        log.info("{} questions retrieved: {}", questionDtos.size(), questionDtos);

        return questionDtos;
    }

    @Transactional
    public QuestionDto createQuestion(NewQuestionRequest request) {
        log.info("Creating new question");

        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        List<Skill> skills = new ArrayList<>();
        if (request.skillNames() != null && !request.skillNames().isEmpty()) {
            request.skillNames().stream()
                    .map(skillName -> skillRepository.findByName(skillName)
                                .orElseGet(() -> skillRepository.save(Skill.builder().name(skillName).build()))
                    ).forEach(skills::add);
        }

        log.info("Skills: {}", skills);

        Question question;
        try {
            question = Question.builder()
                    .title(request.title())
                    .text(request.text())
                    .answer(request.answer())
                    .client(client)
                    .skills(new HashSet<>(skills))
                    .active(true)
                    .build();
            questionRepository.save(question);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }

        QuestionDto questionDto = questionDtoMapper.apply(question);

        log.info("Question created: {}", questionDto);

        return questionDto;
    }

}
