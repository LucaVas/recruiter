package dev.lucavassos.recruiter.modules.question;

import dev.lucavassos.recruiter.modules.question.domain.NewQuestionnaireRequest;
import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionnaireDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1")
public class QuestionController {

    private final QuestionService service;

    @PostMapping("/questionnaires")
    public ResponseEntity<QuestionnaireDto> createQuestionnaire(
            @Valid @RequestBody NewQuestionnaireRequest request) {
        log.debug("Received new request to create questionnaire: {}", request);
        QuestionnaireDto questionnaire = service.createQuestionnaire(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(questionnaire);
    }

    @GetMapping(path = "/questionnaires/search")
    public ResponseEntity<List<QuestionnaireDto>> getQuestionnaires(
            @RequestParam("titleOrClientOrSkill") String findByTitleOrClient) {
        log.debug("Received request to get questionnaires for title / client {}", findByTitleOrClient);
        List<QuestionnaireDto> questionnaires = service.getQuestionnaireByTitleOrClient(findByTitleOrClient);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(questionnaires);
    }
}
