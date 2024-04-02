package dev.lucavassos.recruiter.modules.candidacy.controller;

import dev.lucavassos.recruiter.modules.candidacy.domain.NewCandidacyRequest;
import dev.lucavassos.recruiter.modules.candidacy.service.CandidacyService;
import dev.lucavassos.recruiter.modules.candidate.domain.NewCandidateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1")
public class CandidacyController {

    private static final Logger LOG = LoggerFactory.getLogger(CandidacyController.class);
    @Autowired
    private CandidacyService service;

    @PostMapping("/candidacies")
    public ResponseEntity<?> addCandidacy(
            @RequestBody NewCandidacyRequest candidacyRequest) {
        service.addCandidacy(candidacyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
