package dev.lucavassos.recruiter.modules.candidate.controller;

import dev.lucavassos.recruiter.modules.candidate.domain.NewCandidateRequest;
import dev.lucavassos.recruiter.modules.candidate.domain.CandidateResponse;
import dev.lucavassos.recruiter.modules.candidate.domain.UpdateCandidateRequest;
import dev.lucavassos.recruiter.modules.candidate.repository.dto.CandidateDto;
import dev.lucavassos.recruiter.modules.candidate.service.CandidateService;
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
public class CandidateController {

    private static final Logger LOG = LoggerFactory.getLogger(CandidateController.class);
    @Autowired
    private CandidateService service;

    @PostMapping("/candidates")
    public ResponseEntity<CandidateResponse> addCandidate(
            @Valid @RequestBody NewCandidateRequest request) throws Exception {
        LOG.info("Received new request for candidate: {}", request);
        CandidateResponse response = service.addCandidate(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/candidates")
    public ResponseEntity<List<CandidateDto>> getAllCandidates() {
        LOG.info("Received request for candidates.");
        return new ResponseEntity<>(service.getAllCandidates(), HttpStatus.OK);
    }

    @PutMapping("/candidates")
    public ResponseEntity<CandidateResponse> updateCandidate(
            @Valid @RequestBody UpdateCandidateRequest request) throws Exception {
        LOG.info("Received request to update candidate with pan: {}", request.pan());
        CandidateResponse response = service.updateCandidate(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/candidates/{identifier}")
    public ResponseEntity<CandidateResponse> findCandidate(
            @PathVariable("identifier") String panOrEmailOrPhone) throws Exception {
        LOG.info("New request received to find candidate with identified {}", panOrEmailOrPhone);
        CandidateResponse response = service.findCandidate(panOrEmailOrPhone);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
