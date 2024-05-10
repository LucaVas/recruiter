package dev.lucavassos.recruiter.modules.candidacy;

import dev.lucavassos.recruiter.modules.candidacy.domain.NewCandidacyCommentRequest;
import dev.lucavassos.recruiter.modules.candidacy.domain.NewCandidacyRequest;
import dev.lucavassos.recruiter.modules.candidacy.domain.UpdateCandidacyRequest;
import dev.lucavassos.recruiter.modules.candidacy.repository.dto.CandidacyCommentDto;
import dev.lucavassos.recruiter.modules.candidacy.repository.dto.CandidacyDto;
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
//        service.addCandidacy(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/candidacies/job={jobId}&candidate={pan}")
    public ResponseEntity<CandidacyDto> updateCandidacy(
            @PathVariable Long jobId,
            @PathVariable String pan,
            @Valid @RequestBody UpdateCandidacyRequest request) throws Exception {
        LOG.info("Received request to update candidacy: {}", request);
        return ResponseEntity.ok(service.updateCandidacy(jobId, pan, request));
    }

    @GetMapping("/candidacies/job={jobId}&candidate={pan}")
    public ResponseEntity<CandidacyDto> getCandidacy(
            @PathVariable Long jobId,
            @PathVariable String pan) {
        LOG.info("Received request to get candidacy with job ID {} and candidate pan {}", jobId, pan);
        return ResponseEntity.ok(service.getCandidacy(jobId, pan));
    }

    @GetMapping("/candidacies")
    public ResponseEntity<List<CandidacyDto>> getAllCandidacies() {
        LOG.info("Received request for candidacies.");
        return ResponseEntity.ok(service.getAllCandidacies());
    }

    @GetMapping("/candidacies/job={jobId}&candidate={pan}/comments")
    public ResponseEntity<List<CandidacyCommentDto>> getCandidacyComments(
            @PathVariable Long jobId,
            @PathVariable String pan
    ) {
        LOG.info("Received request to get comments for candidacy with job ID {} and candidate pan {}", jobId, pan);
        return ResponseEntity.ok(service.getCandidacyComments(jobId, pan));
    }

    @PostMapping("/candidacies/job={jobId}&candidate={pan}/comments")
    public ResponseEntity<?> addCandidacyComment(
            @PathVariable Long jobId,
            @PathVariable String pan,
            @Valid @RequestBody NewCandidacyCommentRequest comment
    ) {
        LOG.info("Received request to add comment to candidacy with job ID {} and candidate pan {}", jobId, pan);
        service.addCandidacyComment(jobId, pan, comment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
