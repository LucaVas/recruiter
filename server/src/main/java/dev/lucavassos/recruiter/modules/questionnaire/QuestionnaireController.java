package dev.lucavassos.recruiter.modules.questionnaire;

import dev.lucavassos.recruiter.modules.questionnaire.domain.NewQuestionnaireRequest;
import dev.lucavassos.recruiter.modules.questionnaire.repository.dto.QuestionnaireDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1")
public class QuestionnaireController {

    private final QuestionnaireService service;

//    @PostMapping("/questionnaires")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<QuestionnaireDto> createQuestionnaire(
//            @Valid @RequestBody NewQuestionnaireRequest request) {
//        log.debug("Received new request to create questionnaire: {}", request);
//        QuestionnaireDto questionnaire = service.createQuestionnaire(request);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(questionnaire);
//    }

    @GetMapping("/questionnaires/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<QuestionnaireDto>> getAllQuestionnaires (@RequestParam("client") String clientName) {
        log.debug("Received new request to get questionnaires for client {}", clientName);
        List<QuestionnaireDto> questionnaires = service.getAllQuestionnaires(clientName, 0, 1000);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(questionnaires);
    }
}
