package dev.lucavassos.recruiter.modules.questionnaire;

import dev.lucavassos.recruiter.modules.questionnaire.domain.NewQuestionnaireRequest;
import dev.lucavassos.recruiter.modules.questionnaire.repository.dto.QuestionnaireDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
