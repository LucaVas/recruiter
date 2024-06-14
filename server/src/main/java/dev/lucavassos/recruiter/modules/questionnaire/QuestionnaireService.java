package dev.lucavassos.recruiter.modules.questionnaire;

import dev.lucavassos.recruiter.exception.DatabaseException;
import dev.lucavassos.recruiter.exception.DuplicateResourceException;
import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.modules.client.entities.Client;
import dev.lucavassos.recruiter.modules.client.repository.ClientRepository;
import dev.lucavassos.recruiter.modules.questionnaire.domain.NewQuestionDto;
import dev.lucavassos.recruiter.modules.questionnaire.domain.NewQuestionnaireRequest;
import dev.lucavassos.recruiter.modules.questionnaire.domain.QuestionType;
import dev.lucavassos.recruiter.modules.questionnaire.domain.UpdateQuestionnaireRequest;
import dev.lucavassos.recruiter.modules.questionnaire.entity.Question;
import dev.lucavassos.recruiter.modules.questionnaire.entity.Questionnaire;
import dev.lucavassos.recruiter.modules.questionnaire.repository.QuestionRepository;
import dev.lucavassos.recruiter.modules.questionnaire.repository.QuestionnaireRepository;
import dev.lucavassos.recruiter.modules.questionnaire.repository.dto.QuestionDto;
import dev.lucavassos.recruiter.modules.questionnaire.repository.dto.QuestionnaireDto;
import dev.lucavassos.recruiter.modules.questionnaire.repository.dto.QuestionnaireDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class QuestionnaireService {

    private final ClientRepository clientRepository;
    private final QuestionnaireRepository repository;
    private final QuestionnaireDtoMapper questionnaireDtoMapper;
    private final QuestionRepository questionRepository;


    @Transactional
    public QuestionnaireDto getQuestionnaire(String clientName, String title) {
        Questionnaire questionnaire = repository.findByTitleAndClientName(title, clientName)
                .orElseThrow(() -> new ResourceNotFoundException("Questionnaire not found"));
        return questionnaireDtoMapper.apply(questionnaire);
    }

    @Transactional
    public List<QuestionnaireDto> getAllQuestionnairesByClientOrTitle(String clientOrTitle, Integer pageNumber, Integer pageSize) {

        Pageable limit = PageRequest.of(pageNumber, pageSize);
        log.debug("Retrieving {} questionnaires", pageSize);

        List<Questionnaire> questionnaires = repository.findByTitleOrClientName(clientOrTitle, clientOrTitle, limit);

        log.debug("Questionnaires retrieved: {}", questionnaires);
        return questionnaires.stream().map(questionnaireDtoMapper).collect(Collectors.toList());
    }

    @Transactional
    public List<QuestionnaireDto> getAllQuestionnaires(Integer pageNumber, Integer pageSize) {

        Pageable limit = PageRequest.of(pageNumber, pageSize);
        log.debug("Retrieving {} questionnaires", pageSize);

        List<Questionnaire> questionnaires = repository.findAll(limit).stream().toList();

        log.debug("Questionnaires retrieved: {}", questionnaires);
        return questionnaires.stream().map(questionnaireDtoMapper).collect(Collectors.toList());
    }

    @Transactional
    public QuestionnaireDto saveQuestionnaire(NewQuestionnaireRequest request) {

        Questionnaire questionnaire = buildQuestionnaire(request);

        if (repository.existsByClientNameAndTitle(questionnaire.getClient().getName(), questionnaire.getTitle())) {
            throw new DatabaseException("Questionnaire with this title already exists for this client");
        }

        try {
            Questionnaire createdQuestionnaire = repository.save(questionnaire);
            return questionnaireDtoMapper.apply(createdQuestionnaire);
        } catch (Exception e) {
            log.error("Error while saving questionnaire: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    Questionnaire updateQuestionnaire(Questionnaire questionnaire) {
        try {
            Questionnaire saved = repository.saveAndFlush(questionnaire);
            return saved;
        } catch (Exception e) {
            log.error("Error while updating questionnaire: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public QuestionnaireDto updateQuestionnaire(String title, UpdateQuestionnaireRequest request) {

        Questionnaire questionnaire = repository.findByTitle(title).orElseThrow(() -> new ResourceNotFoundException("Questionnaire does not exist"));
        if (repository.existsByClientNameAndTitle(questionnaire.getClient().getName(), request.getTitle())) {
            throw new DuplicateResourceException("Questionnaire with title %s and client %s already exists".formatted(title, questionnaire.getClient().getName()));
        }

        Set<Question> questions = updateQuestions(request);
        questionnaire.setTitle(request.getTitle());
        questionnaire.setQuestions(questions);

        Questionnaire saved = updateQuestionnaire(questionnaire);

        return questionnaireDtoMapper.apply(questionnaire);
    }

    private Questionnaire buildQuestionnaire(NewQuestionnaireRequest request) {
        Client client = clientRepository.findByName(request.getClient().getName())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        Set<Question> questions = request.getQuestions().stream()
                .map(this::buildQuestion)
                .collect(Collectors.toSet());

        return Questionnaire.builder()
                .title(request.getTitle())
                .questions(questions)
                .client(client)
                .build();
    }

    private Question buildQuestion(NewQuestionDto newQuestionDto) {
        return Question.builder()
                .text(newQuestionDto.text())
                .answer(newQuestionDto.answer())
                .questionType(newQuestionDto.questionType())
                .build();
    }


    private Set<Question> updateQuestions(UpdateQuestionnaireRequest request) {
        Set<Question> questions = new HashSet<>();
        for (QuestionDto questionDto : request.getQuestions()) {
            Question question;
            if (questionDto.id() == null) {
                question = Question.builder()
                        .text(questionDto.text())
                        .answer(questionDto.answer())
                        .questionType(questionDto.questionType())
                        .build();
            } else {
                question = questionRepository
                        .findById(questionDto.id()).orElseThrow(() -> new ResourceNotFoundException("Question not found"));
                if (!question.getText().equals(questionDto.text())) {
                    question.setText(questionDto.text());
                }
                if (!question.getQuestionType().equals(QuestionType.OPEN_QUESTION) && !question.getAnswer().equals(questionDto.answer())) {
                    question.setAnswer(questionDto.answer());
                }
                if (!question.getQuestionType().equals(questionDto.questionType())) {
                    question.setQuestionType(questionDto.questionType());
                }
            }
            questions.add(question);
        }
        return questions;
    }

}
