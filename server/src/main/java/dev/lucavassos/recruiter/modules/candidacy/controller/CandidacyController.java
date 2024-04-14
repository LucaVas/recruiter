package dev.lucavassos.recruiter.modules.candidacy.controller;

import dev.lucavassos.recruiter.modules.candidacy.domain.CandidacyResponse;
import dev.lucavassos.recruiter.modules.candidacy.domain.NewCandidacyRequest;
import dev.lucavassos.recruiter.modules.candidacy.domain.UpdateCandidacyRequest;
import dev.lucavassos.recruiter.modules.candidacy.repository.dto.CandidacyDto;
import dev.lucavassos.recruiter.modules.candidacy.service.CandidacyService;
import dev.lucavassos.recruiter.modules.candidate.domain.NewCandidateRequest;
import dev.lucavassos.recruiter.modules.candidate.repository.dto.CandidateDto;
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
public class CandidacyController {

    private static final Logger LOG = LoggerFactory.getLogger(CandidacyController.class);
    @Autowired
    private CandidacyService service;

    @PostMapping("/candidacies")
    public ResponseEntity<?> addCandidacy(
            @Valid @RequestBody NewCandidacyRequest request) {
        LOG.info("Received request to add candidacy: {}", request);
        service.addCandidacy(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/candidacies/{id}")
    public ResponseEntity<CandidacyResponse> updateCandidacy(
            @Valid @RequestBody UpdateCandidacyRequest request) throws Exception {
        LOG.info("Received request to update candidacy: {}", request);
        return ResponseEntity.ok(service.updateCandidacy(request));
    }

    @GetMapping("/candidacies/{id}")
    public ResponseEntity<CandidacyResponse> getCandidacy(
            @PathVariable Long id) {
        LOG.info("Received request to get candidacy: {}", id);
        return ResponseEntity.ok(service.getCandidacy(id));
    }

    @GetMapping("/candidacies")
    public ResponseEntity<List<CandidacyDto>> getAllCandidacies() {
        LOG.info("Received request for candidacies.");
        return new ResponseEntity<>(service.getAllCandidacies(), HttpStatus.OK);
    }
}
