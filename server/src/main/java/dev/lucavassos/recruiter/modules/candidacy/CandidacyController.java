package dev.lucavassos.recruiter.modules.candidacy;

import dev.lucavassos.recruiter.modules.candidacy.domain.NewCandidacyCommentRequest;
import dev.lucavassos.recruiter.modules.candidacy.domain.NewCandidacyRequest;
import dev.lucavassos.recruiter.modules.candidacy.domain.UpdateCandidacyRequest;
import dev.lucavassos.recruiter.modules.candidacy.repository.dto.CandidacyCommentDto;
import dev.lucavassos.recruiter.modules.candidacy.repository.dto.CandidacyDto;
import dev.lucavassos.recruiter.modules.candidacy.repository.dto.CandidacyFileDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/candidacies")
public class CandidacyController {

    private final CandidacyService service;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addCandidacy(@Valid NewCandidacyRequest request) {
        log.info("Received request to add candidacy: {}", request);
        service.addCandidacy(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidacyDto> updateCandidacy(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCandidacyRequest request) {
        log.info("Received request to update candidacy: {}", request);
        return ResponseEntity.ok(service.updateCandidacy(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidacyDto> getCandidacy(
            @PathVariable Long id
    ) {
        log.info("Received request to get candidacy with ID {}", id);
        return ResponseEntity.ok(service.getCandidacy(id));
    }

    @GetMapping
    public ResponseEntity<List<CandidacyDto>> getAllCandidacies() {
        log.info("Received request for candidacies.");
        return ResponseEntity.ok(service.getAllCandidacies());
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CandidacyCommentDto>> getCandidacyComments(
            @PathVariable Long id
    ) {
        log.info("Received request to get comments for candidacy with ID {}", id);
        return ResponseEntity.ok(service.getCandidacyComments(id));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addCandidacyComment(
            @PathVariable Long id,
            @Valid @RequestBody NewCandidacyCommentRequest comment
    ) {
        log.info("Received request to add comment to candidacy with ID {}", id);
        service.addCandidacyComment(id, comment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCandidacy(
            @PathVariable Long id
    ) {
        log.info("Received request to delete candidacy with ID {}", id);
        service.deleteCandidacy(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/files")
    public ResponseEntity<List<CandidacyFileDto>> getCandidacyFiles(
            @PathVariable Long id
    ) {
        log.info("Received request to get files for candidacy with ID {}", id);
        return ResponseEntity.ok(service.getCandidacyFiles(id));
    }

    @DeleteMapping("/files/{fileId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> deleteCandidacyFile(@PathVariable("fileId") Long fileId) {
        log.info("Received request to delete candidacy file with ID {}", fileId);
        service.deleteCandidacyFile(fileId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/files/{fileId}")
    public ResponseEntity<byte[]> getCandidacyFile(@PathVariable("fileId") Long fileId) {
        log.info("Received request to get candidacy file with ID {}", fileId);
        byte[] file = service.getCandidacyFile(fileId);
        log.info("File retrieved successfully.");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentLength(file.length);

        return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/files",
            method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFiles(
            @PathVariable Long id,
            @RequestParam("files") List<MultipartFile> files
    ) {

        log.info("Received request to add {} files to candidacy with ID {}", files.size(), id);
        service.addFilesToCandidacy(id, files);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
