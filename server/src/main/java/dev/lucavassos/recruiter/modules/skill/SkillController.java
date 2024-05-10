package dev.lucavassos.recruiter.modules.skill;

import dev.lucavassos.recruiter.modules.question.domain.NewQuestionRequest;
import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDto;
import dev.lucavassos.recruiter.modules.skill.domain.NewSkillRequest;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "api/v1")
public class SkillController {

    @Autowired
    private SkillService service;


    @GetMapping("/skills")
    public ResponseEntity<List<SkillDto>> getAllSkills() {
        log.info("Received request for all skills.");
        return ResponseEntity.ok(service.getAllSkills(0, 2000));
    }

    @PostMapping("/skills")
    public ResponseEntity<SkillDto> createSkill(
            @Valid @RequestBody NewSkillRequest request) throws Exception {
        log.info("Received new request to create skill: {}", request);
        SkillDto skill = service.createSkill(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(skill);
    }
}
