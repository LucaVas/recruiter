package dev.lucavassos.recruiter.modules.questionnaire;

import dev.lucavassos.recruiter.modules.job.domain.NewJobRequest;
import dev.lucavassos.recruiter.modules.job.repository.dto.JobDto;
import dev.lucavassos.recruiter.modules.questionnaire.domain.NewQuestionnaireRequest;
import dev.lucavassos.recruiter.modules.questionnaire.repository.dto.QuestionnaireDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/questionnaires")
public class QuestionnaireController {

    private final QuestionnaireService service;

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<QuestionnaireDto>> getAllQuestionnairesByClientOrTitle(@RequestParam("clientOrTitle") String clientOrTitle) {
        log.debug("Received new request to get questionnaires for client/title {}", clientOrTitle);
        List<QuestionnaireDto> questionnaires = service.getAllQuestionnairesByClientOrTitle(clientOrTitle, 0, 1000);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(questionnaires);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<QuestionnaireDto>> getAllQuestionnaires() {
        log.debug("Received new request to get all questionnaires");
        List<QuestionnaireDto> questionnaires = service.getAllQuestionnaires(0, 1000);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(questionnaires);
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QuestionnaireDto> saveQuestionnaire(
            @Valid @RequestBody NewQuestionnaireRequest request) {
        log.debug("Received request to save new questionnaire: {}", request);
        return new ResponseEntity<>(service.saveQuestionnaire(request), HttpStatus.CREATED);
    }
}
