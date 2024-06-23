package dev.lucavassos.recruiter.modules.questionnaire;

import dev.lucavassos.recruiter.exception.DatabaseException;
import dev.lucavassos.recruiter.exception.DuplicateResourceException;
import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.modules.HistoryEventType;
import dev.lucavassos.recruiter.modules.client.entities.Client;
import dev.lucavassos.recruiter.modules.client.repository.ClientRepository;
import dev.lucavassos.recruiter.modules.questionnaire.domain.NewQuestionDto;
import dev.lucavassos.recruiter.modules.questionnaire.domain.NewQuestionnaireRequest;
import dev.lucavassos.recruiter.modules.questionnaire.domain.QuestionType;
import dev.lucavassos.recruiter.modules.questionnaire.domain.UpdateQuestionnaireRequest;
import dev.lucavassos.recruiter.modules.questionnaire.entity.Question;
import dev.lucavassos.recruiter.modules.questionnaire.entity.QuestionHistory;
import dev.lucavassos.recruiter.modules.questionnaire.entity.Questionnaire;
import dev.lucavassos.recruiter.modules.questionnaire.entity.QuestionnaireHistory;
import dev.lucavassos.recruiter.modules.questionnaire.repository.QuestionHistoryRepository;
import dev.lucavassos.recruiter.modules.questionnaire.repository.QuestionRepository;
import dev.lucavassos.recruiter.modules.questionnaire.repository.QuestionnaireHistoryRepository;
import dev.lucavassos.recruiter.modules.questionnaire.repository.QuestionnaireRepository;
import dev.lucavassos.recruiter.modules.questionnaire.repository.dto.QuestionDto;
import dev.lucavassos.recruiter.modules.questionnaire.repository.dto.QuestionnaireDto;
import dev.lucavassos.recruiter.modules.questionnaire.repository.dto.QuestionnaireDtoMapper;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class QuestionnaireService {

    private final ClientRepository clientRepository;
    private final QuestionnaireRepository repository;
    private final QuestionnaireHistoryRepository historyRepository;
    private final QuestionnaireDtoMapper questionnaireDtoMapper;
    private final QuestionRepository questionRepository;
    private final QuestionHistoryRepository questionHistoryRepository;
    private final UserRepository userRepository;


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
            saveQuestionnaireHistoryEvent(createdQuestionnaire, HistoryEventType.CREATED);
            return questionnaireDtoMapper.apply(createdQuestionnaire);
        } catch (Exception e) {
            log.error("Error while saving questionnaire: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public QuestionnaireDto updateQuestionnaire(Long id, UpdateQuestionnaireRequest request) {

        Questionnaire questionnaire = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Questionnaire does not exist"));

        String newTitle = request.getTitle();

        if (!questionnaire.getTitle().equals(newTitle) && repository.existsByClientNameAndTitle(questionnaire.getClient().getName(), newTitle)) {
            throw new DuplicateResourceException("Questionnaire with this title already exists for this client");
        }

        Set<Question> questions = updateQuestions(request, questionnaire.getTitle());

        questionnaire.setTitle(newTitle);
        questionnaire.setQuestions(questions);

        questions.forEach(question -> {
            question.setQuestionnaire(questionnaire);
            saveQuestionHistoryEvent(question, HistoryEventType.UPDATED);
        });

        Questionnaire saved = saveQuestionnaire(questionnaire);
        saveQuestionnaireHistoryEvent(saved, HistoryEventType.UPDATED);

        return questionnaireDtoMapper.apply(saved);
    }

    @Transactional
    Questionnaire saveQuestionnaire(Questionnaire questionnaire) {
        try {
            return repository.saveAndFlush(questionnaire);
        } catch (Exception e) {
            log.error("Error while updating questionnaire: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }

    private Questionnaire buildQuestionnaire(NewQuestionnaireRequest request) {
        Client client = clientRepository.findByName(request.getClient().getName())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        Set<Question> questions = request.getQuestions().stream()
                .map(this::buildQuestion)
                .collect(Collectors.toSet());

        Questionnaire questionnaire = Questionnaire.builder()
                .title(request.getTitle())
                .questions(questions)
                .client(client)
                .build();
        questions
                .forEach(question -> {
                    question.setQuestionnaire(questionnaire);
                    saveQuestionHistoryEvent(question, HistoryEventType.CREATED);
                });
        return questionnaire;
    }

    private Question buildQuestion(NewQuestionDto newQuestionDto) {
        return Question.builder()
                .text(newQuestionDto.text())
                .answer(newQuestionDto.answer())
                .questionType(newQuestionDto.questionType())
                .build();
    }

    private Set<Question> updateQuestions(UpdateQuestionnaireRequest request, String title) {
        Set<Question> questions = new HashSet<>();
        for (QuestionDto questionDto : request.getQuestions()) {

            // check if question exists for this questionnaire
            Optional<Question> exist = questionRepository.findByTextAndQuestionnaireTitle(questionDto.getText(), title);
            if (exist.isEmpty()) {
                Question newQuestion = Question.builder()
                        .text(questionDto.getText())
                        .answer(questionDto.getAnswer())
                        .questionType(questionDto.getQuestionType())
                        .build();
                questions.add(newQuestion);
            } else {
                Question question = updateQuestion(questionDto, exist.get());
                questions.add(question);
            }
        }
        return questions;
    }

    private Question updateQuestion(QuestionDto questionDto, Question question) {
        if (!question.getText().equals(questionDto.getText())) {
            question.setText(questionDto.getText());
        }
        if (!question.getQuestionType().equals(QuestionType.OPEN_QUESTION) && !question.getAnswer().equals(questionDto.getAnswer())) {
            question.setAnswer(questionDto.getAnswer());
        }
        if (!question.getQuestionType().equals(questionDto.getQuestionType())) {
            question.setQuestionType(questionDto.getQuestionType());
        }
        return question;
    }

    @Transactional
    private void saveQuestionnaireHistoryEvent(Questionnaire questionnaire, HistoryEventType eventType) {

        try {
            User user = getAuthUser();
            QuestionnaireHistory event = QuestionnaireHistory.builder()
                    .title(questionnaire.getTitle())
                    .eventType(eventType)
                    .questionnaire(questionnaire)
                    .modifiedBy(user)
                    .build();
            historyRepository.save(event);
        } catch (Exception e) {
            log.error("Database error while saving questionnaire history event: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    private void saveQuestionHistoryEvent(Question question, HistoryEventType eventType) {

        try {
            User user = getAuthUser();
            QuestionHistory event = QuestionHistory.builder()
                    .text(question.getText())
                    .answer(question.getAnswer())
                    .questionType(question.getQuestionType())
                    .eventType(eventType)
                    .question(question)
                    .modifiedBy(user)
                    .build();
            questionHistoryRepository.save(event);
        } catch (Exception e) {
            log.error("Database error while saving question history event: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    private User getAuthUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User userPrincipal = (User) authentication.getPrincipal();
        return userRepository.findOneById(userPrincipal.getId()).orElseThrow(
                () -> {
                    log.error("User with id {} not found", userPrincipal.getId());
                    return new ResourceNotFoundException("User not found");
                }
        );
    }

}
