package dev.lucavassos.recruiter.modules.skill;

import dev.lucavassos.recruiter.modules.skill.domain.NewSkillRequest;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDto;
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
public class SkillController {

    private final SkillService service;


    @GetMapping("/skills")
    public ResponseEntity<List<SkillDto>> getAllSkills() {
        log.debug("Received request for all skills.");
        return ResponseEntity.ok(service.getAllSkills(0, 2000));
    }

    @PostMapping("/skills")
    public ResponseEntity<SkillDto> createSkill(
            @Valid @RequestBody NewSkillRequest request) {
        log.debug("Received new request to create skill: {}", request);
        SkillDto skill = service.createSkill(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(skill);
    }
}
