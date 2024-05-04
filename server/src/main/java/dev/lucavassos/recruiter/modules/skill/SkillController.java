package dev.lucavassos.recruiter.modules.skill;

import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1")
public class SkillController {

    private static final Logger LOG = LoggerFactory.getLogger(SkillController.class);

    @Autowired
    private SkillService service;


    @GetMapping("/skills")
    public ResponseEntity<List<SkillDto>> getAllSkills() {
        LOG.info("Received request for all skills.");
        return new ResponseEntity<>(service.getAllSkills(), HttpStatus.OK);
    }
}
