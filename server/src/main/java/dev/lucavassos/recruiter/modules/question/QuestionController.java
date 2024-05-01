package dev.lucavassos.recruiter.modules.question;

import dev.lucavassos.recruiter.modules.question.domain.NewQuestionRequest;
import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1")
public class QuestionController {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService service;

    @PostMapping("/questions")
    public ResponseEntity<QuestionDto> createQuestion(
            @Valid @RequestBody NewQuestionRequest request) throws Exception {
        LOG.info("Received new request to create question: {}", request);
        QuestionDto question = service.createQuestion(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(question);
    }

    @GetMapping(path = "/questions/search")
    public ResponseEntity<List<QuestionDto>> getQuestions(
            @RequestParam("titleOrClientOrSkill") String findByTitleOrClient) throws Exception {
        LOG.info("Received request to get questions for ttle / client {}", findByTitleOrClient);
        List<QuestionDto> questions = service.getQuestionsByTitleOrClient(findByTitleOrClient);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(questions);
    }
}
