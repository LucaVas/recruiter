package dev.lucavassos.recruiter.modules.job;

import dev.lucavassos.recruiter.modules.job.domain.ChangeJobStatusRequest;
import dev.lucavassos.recruiter.modules.job.domain.JobResponse;
import dev.lucavassos.recruiter.modules.job.domain.NewJobRequest;
import dev.lucavassos.recruiter.modules.job.domain.UpdateJobRequest;
import dev.lucavassos.recruiter.modules.job.repository.dto.JobDto;
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
@RequestMapping(value = "api/v1")
public class JobController {

    private final JobService service;

    @PostMapping("/jobs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobResponse> addJob(
            @Valid @RequestBody NewJobRequest request) {
        log.debug("Received request to add new job: {}", request);
        return new ResponseEntity<>(service.addJob(request), HttpStatus.CREATED);
    }

    @PutMapping("/jobs/{jobId}")
    public ResponseEntity<JobResponse> updateJob(
            @Valid @RequestBody UpdateJobRequest request) {
        log.debug("Received request to update job");
        return new ResponseEntity<>(service.updateJob(request), HttpStatus.CREATED);
    }

    @PostMapping("/jobs/status/{jobId}")
    public ResponseEntity<?> changeJobStatus(
            @PathVariable("jobId") Long id,
            @Valid @RequestBody ChangeJobStatusRequest request) {
        log.debug("Received request to change job status: {}", request);
        service.changeJobStatus(id, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/jobs/{jobId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteJob(@PathVariable("jobId") Long id) {
        log.debug("Received request to delete job: {}", id);
        service.deleteJob(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobDto>> getAllJobs() {
        log.debug("Received request for all jobs.");
        return new ResponseEntity<>(service.getAllJobs(0, 1000), HttpStatus.OK);
    }

    @GetMapping("/jobs/{jobId}")
    public ResponseEntity<JobDto> getJob(@PathVariable("jobId") Long id) {
        log.debug("Received request to get details for job: {}", id);
        JobDto job = service.getJobById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(job);
    }
}
