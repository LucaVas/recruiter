package dev.lucavassos.recruiter.modules.job.controller;

import dev.lucavassos.recruiter.modules.job.domain.JobResponse;
import dev.lucavassos.recruiter.modules.job.domain.NewJobRequest;
import dev.lucavassos.recruiter.modules.job.repository.dto.JobDto;
import dev.lucavassos.recruiter.modules.job.service.JobService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1")
public class JobController {

    private static final Logger LOG = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private JobService service;

    @PostMapping("/jobs")
    public ResponseEntity<JobResponse> addJob(
            @RequestBody NewJobRequest request) throws Exception {
        LOG.info("Received request to add new job: {}", request);
        return new ResponseEntity<>(service.addJob(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/jobs/{jobId}")
    public ResponseEntity<?> deleteJob(@PathVariable("jobId") Long id) throws Exception {
        LOG.info("Received request to delete job: {}", id);
        service.deleteJob(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobDto>> getAllJobs() {
        LOG.info("Received request for all jobs.");
        return new ResponseEntity<>(service.getAllJobs(), HttpStatus.OK);
    }
}
