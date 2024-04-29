package dev.lucavassos.recruiter.modules.question;

import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDto;
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

    @GetMapping(path = "/questions/search")
    public ResponseEntity<List<QuestionDto>> getQuestions(
            @RequestParam("clientOrSkill") String clientOrSkill) throws Exception {
        LOG.info("Received request to get questions for client / skill {}", clientOrSkill);
        List<QuestionDto> questions = service.getQuestionsByClientOrSkill(clientOrSkill);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(questions);
    }
}
