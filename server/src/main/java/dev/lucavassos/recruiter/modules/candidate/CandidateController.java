package dev.lucavassos.recruiter.modules.candidate;

import dev.lucavassos.recruiter.modules.candidate.domain.CandidateResponse;
import dev.lucavassos.recruiter.modules.candidate.domain.NewCandidateRequest;
import dev.lucavassos.recruiter.modules.candidate.domain.UpdateCandidateRequest;
import dev.lucavassos.recruiter.modules.candidate.repository.dto.CandidateDto;
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
public class CandidateController {

    private final CandidateService service;

    @PostMapping("/candidates")
    public ResponseEntity<CandidateResponse> addCandidate(
            @Valid @RequestBody NewCandidateRequest request) throws Exception {
        log.info("Received new request for candidate: {}", request);
        CandidateResponse response = service.addCandidate(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/candidates")
    public ResponseEntity<List<CandidateDto>> getAllCandidates() {
        log.info("Received request for candidates.");
        return new ResponseEntity<>(service.getAllCandidates(), HttpStatus.OK);
    }

    @PutMapping("/candidates")
    public ResponseEntity<CandidateResponse> updateCandidate(
            @Valid @RequestBody UpdateCandidateRequest request) throws Exception {
        log.info("Received request to update candidate with pan: {}", request.pan());
        CandidateResponse response = service.updateCandidate(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/candidates/{identifier}")
    public ResponseEntity<CandidateResponse> findCandidate(
            @PathVariable("identifier") String panOrEmailOrPhone) throws Exception {
        log.info("New request received to find candidate with identified {}", panOrEmailOrPhone);
        CandidateResponse response = service.findCandidate(panOrEmailOrPhone);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
