package dev.lucavassos.recruiter.modules.candidacy;

import dev.lucavassos.recruiter.modules.candidacy.domain.NewCandidacyCommentRequest;
import dev.lucavassos.recruiter.modules.candidacy.domain.NewCandidacyRequest;
import dev.lucavassos.recruiter.modules.candidacy.domain.UpdateCandidacyRequest;
import dev.lucavassos.recruiter.modules.candidacy.repository.dto.CandidacyCommentDto;
import dev.lucavassos.recruiter.modules.candidacy.repository.dto.CandidacyDto;
import dev.lucavassos.recruiter.modules.candidacy.repository.dto.CandidacyFileDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "api/v1")
public class CandidacyController {

    @Autowired
    private CandidacyService service;

    @RequestMapping(value = "/candidacies", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addCandidacy(@Valid NewCandidacyRequest request) {
        log.info("Received request to add candidacy: {}", request);
        service.addCandidacy(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/candidacies/job={jobId}&candidate={pan}")
    public ResponseEntity<CandidacyDto> updateCandidacy(
            @PathVariable Long jobId,
            @PathVariable String pan,
            @Valid @RequestBody UpdateCandidacyRequest request) throws Exception {
        log.info("Received request to update candidacy: {}", request);
        return ResponseEntity.ok(service.updateCandidacy(jobId, pan, request));
    }

    @GetMapping("/candidacies/job={jobId}&candidate={pan}")
    public ResponseEntity<CandidacyDto> getCandidacy(
            @PathVariable Long jobId,
            @PathVariable String pan) {
        log.info("Received request to get candidacy with job ID {} and candidate pan {}", jobId, pan);
        return ResponseEntity.ok(service.getCandidacy(jobId, pan));
    }

    @GetMapping("/candidacies")
    public ResponseEntity<List<CandidacyDto>> getAllCandidacies() {
        log.info("Received request for candidacies.");
        return ResponseEntity.ok(service.getAllCandidacies());
    }

    @GetMapping("/candidacies/job={jobId}&candidate={pan}/comments")
    public ResponseEntity<List<CandidacyCommentDto>> getCandidacyComments(
            @PathVariable Long jobId,
            @PathVariable String pan
    ) {
        log.info("Received request to get comments for candidacy with job ID {} and candidate pan {}", jobId, pan);
        return ResponseEntity.ok(service.getCandidacyComments(jobId, pan));
    }

    @PostMapping("/candidacies/job={jobId}&candidate={pan}/comments")
    public ResponseEntity<?> addCandidacyComment(
            @PathVariable Long jobId,
            @PathVariable String pan,
            @Valid @RequestBody NewCandidacyCommentRequest comment
    ) {
        log.info("Received request to add comment to candidacy with job ID {} and candidate pan {}", jobId, pan);
        service.addCandidacyComment(jobId, pan, comment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/candidacies/job={jobId}&candidate={pan}")
    public ResponseEntity<?> deleteCandidacy(
            @PathVariable Long jobId,
            @PathVariable String pan
    ) {
        log.info("Received request to delete candidacy with job ID {} and candidate pan {}", jobId, pan);
        service.deleteCandidacy(jobId, pan);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/candidacies/job={jobId}&candidate={pan}/files")
    public ResponseEntity<List<CandidacyFileDto>> getCandidacyFiles(
            @PathVariable Long jobId,
            @PathVariable String pan
    ) {
        log.info("Received request to get files for candidacy with job ID {} and candidate pan {}", jobId, pan);
        return ResponseEntity.ok(service.getCandidacyFiles(jobId, pan));
    }

    @DeleteMapping("/candidacies/files/{fileId}")
    public ResponseEntity<?> deleteCandidacyFile(@PathVariable("fileId") Long fileId) {
        log.info("Received request to delete candidacy file with ID {}", fileId);
        service.deleteCandidacyFile(fileId);
        return ResponseEntity.noContent().build();
    }
}
