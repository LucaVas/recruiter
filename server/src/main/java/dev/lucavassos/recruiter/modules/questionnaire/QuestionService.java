package dev.lucavassos.recruiter.modules.questionnaire;

import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.exception.ServerException;
import dev.lucavassos.recruiter.modules.client.entities.Client;
import dev.lucavassos.recruiter.modules.client.repository.ClientRepository;
import dev.lucavassos.recruiter.modules.questionnaire.domain.NewQuestion;
import dev.lucavassos.recruiter.modules.questionnaire.domain.NewQuestionnaireRequest;
import dev.lucavassos.recruiter.modules.questionnaire.entity.Question;
import dev.lucavassos.recruiter.modules.questionnaire.entity.Questionnaire;
import dev.lucavassos.recruiter.modules.questionnaire.entity.QuestionnaireId;
import dev.lucavassos.recruiter.modules.questionnaire.repository.QuestionnaireRepository;
import dev.lucavassos.recruiter.modules.questionnaire.repository.dto.QuestionDtoMapper;
import dev.lucavassos.recruiter.modules.questionnaire.repository.dto.QuestionnaireDto;
import dev.lucavassos.recruiter.modules.questionnaire.repository.dto.QuestionnaireDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class QuestionService {

    private final ClientRepository clientRepository;
    private final QuestionnaireRepository questionnaireRepository;
    private final QuestionDtoMapper questionDtoMapper;
    private final QuestionnaireDtoMapper questionnaireDtoMapper;


    @Transactional
    public QuestionnaireDto createQuestionnaire(NewQuestionnaireRequest request) {

        Questionnaire questionnaire = buildQuestionnaire(request);

        try {
            Questionnaire createdQuestionnaire = questionnaireRepository.save(questionnaire);
            return questionnaireDtoMapper.apply(createdQuestionnaire);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    private Questionnaire buildQuestionnaire(NewQuestionnaireRequest request) {
        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        Set<Question> questions = request.questions().stream()
                .map(this::buildQuestion)
                .collect(Collectors.toSet());

        QuestionnaireId id = new QuestionnaireId(request.title(), client.getName());
        return Questionnaire.builder()
                .id(id)
                .questions(questions)
                .build();
    }

    private Question buildQuestion(NewQuestion newQuestion) {
        return Question.builder()
                .text(newQuestion.text())
                .answer(newQuestion.answer())
                .questionType(newQuestion.type())
                .build();
    }

}
