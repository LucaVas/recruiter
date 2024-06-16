package dev.lucavassos.recruiter.modules.job;

import dev.lucavassos.recruiter.modules.job.domain.ChangeJobStatusRequest;
import dev.lucavassos.recruiter.modules.job.domain.NewJobRequest;
import dev.lucavassos.recruiter.modules.job.domain.UpdateJobRequest;
import dev.lucavassos.recruiter.modules.job.repository.dto.JobDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/jobs")
public class JobController {

    private final JobService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobDTO> addJob(
            @Valid @RequestBody NewJobRequest request) {
        log.debug("Received request to add new job: {}", request);
        return new ResponseEntity<>(service.addJob(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobDTO> updateJob(
            @Valid @RequestBody UpdateJobRequest request) {
        log.debug("Received request to update job");
        return new ResponseEntity<>(service.updateJob(request), HttpStatus.CREATED);
    }

    @PostMapping("/status/{id}")
    public ResponseEntity<?> changeJobStatus(
            @PathVariable("id") Long id,
            @Valid @RequestBody ChangeJobStatusRequest request) {
        log.debug("Received request to change job status: {}", request);
        service.changeJobStatus(id, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteJob(@PathVariable("id") Long id) {
        log.debug("Received request to delete job: {}", id);
        service.deleteJob(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<JobDTO>> getAllJobs() {
        log.debug("Received request for all jobs.");
        return new ResponseEntity<>(service.getAllJobs(0, 1000), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getJob(@PathVariable("id") Long id) {
        log.info("Received request to get details for job with ID {}", id);
        JobDTO job = service.getJobById(id);
        log.info("Job retrieved: [{}]", job);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(job);
    }
}
