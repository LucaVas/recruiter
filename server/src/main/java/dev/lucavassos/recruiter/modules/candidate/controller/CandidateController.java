package dev.lucavassos.recruiter.modules.candidate.controller;

import dev.lucavassos.recruiter.modules.candidate.domain.NewCandidateRequest;
import dev.lucavassos.recruiter.modules.candidate.domain.NewCandidateResponse;
import dev.lucavassos.recruiter.modules.candidate.service.CandidateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
