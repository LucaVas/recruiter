package dev.lucavassos.recruiter.modules.candidate.controller;

import dev.lucavassos.recruiter.modules.candidate.domain.NewCandidateRequest;
import dev.lucavassos.recruiter.modules.candidate.domain.NewCandidateResponse;
import dev.lucavassos.recruiter.modules.candidate.repository.dto.CandidateDto;
import dev.lucavassos.recruiter.modules.candidate.service.CandidateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1")
public class CandidateController {

    private static final Logger LOG = LoggerFactory.getLogger(CandidateController.class);
    @Autowired
    private CandidateService service;

    @PostMapping("/candidates")
    public ResponseEntity<NewCandidateResponse> addCandidate(
            @RequestBody NewCandidateRequest request) throws Exception {
        NewCandidateResponse response = service.addCandidate(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/candidates/pan/{pan}")
    public ResponseEntity<CandidateDto> findCandidateByPan(
            @PathVariable("pan") String pan) throws Exception {
        LOG.info("New request received for candidate with PAN {}", pan);
        CandidateDto candidate = service.findCandidateByPan(pan);
        return ResponseEntity.status(HttpStatus.OK)
                .body(candidate);
    }
}
