package dev.lucavassos.recruiter.modules.questionnaire;

import dev.lucavassos.recruiter.exception.DatabaseException;
import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.modules.client.entities.Client;
import dev.lucavassos.recruiter.modules.client.repository.ClientRepository;
import dev.lucavassos.recruiter.modules.questionnaire.domain.NewQuestion;
import dev.lucavassos.recruiter.modules.questionnaire.domain.NewQuestionnaireRequest;
import dev.lucavassos.recruiter.modules.questionnaire.entity.Question;
import dev.lucavassos.recruiter.modules.questionnaire.entity.Questionnaire;
import dev.lucavassos.recruiter.modules.questionnaire.entity.QuestionnaireId;
import dev.lucavassos.recruiter.modules.questionnaire.repository.QuestionnaireRepository;
import dev.lucavassos.recruiter.modules.questionnaire.repository.dto.QuestionnaireDto;
import dev.lucavassos.recruiter.modules.questionnaire.repository.dto.QuestionnaireDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional
    public List<QuestionnaireDto> getAllQuestionnairesByClientOrTitle(String clientOrTitle, Integer pageNumber, Integer pageSize) {

        Pageable limit = PageRequest.of(pageNumber, pageSize);
        log.debug("Retrieving {} questionnaires", pageSize);

        List<Questionnaire> questionnaires = repository.findByIdTitleOrIdClientName(clientOrTitle, clientOrTitle, limit);

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

        if (repository.existsByIdClientNameAndIdTitle(questionnaire.getId().getClientName(), questionnaire.getId().getTitle())) {
            throw new DatabaseException("Questionnaire already exists");
        }

        try {
            Questionnaire createdQuestionnaire = repository.save(questionnaire);
            return questionnaireDtoMapper.apply(createdQuestionnaire);
        } catch (Exception e) {
            log.error("Error while saving questionnaire: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }

    private Questionnaire buildQuestionnaire(NewQuestionnaireRequest request) {
        Client client = clientRepository.findByName(request.clientName())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        Set<Question> questions = request.questions().stream()
                .map(this::buildQuestion)
                .collect(Collectors.toSet());

        QuestionnaireId id = new QuestionnaireId(request.title(), client.getName());
        return Questionnaire.builder()
                .id(id)
                .questions(questions)
                .client(client)
                .build();
    }

    private Question buildQuestion(NewQuestion newQuestion) {
        return Question.builder()
                .text(newQuestion.text())
                .answer(newQuestion.answer())
                .questionType(newQuestion.questionType())
                .build();
    }

}
