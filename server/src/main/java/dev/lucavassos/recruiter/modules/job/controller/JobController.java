package dev.lucavassos.recruiter.modules.job.controller;

import dev.lucavassos.recruiter.modules.job.repository.dto.JobDto;
import dev.lucavassos.recruiter.modules.job.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1")
public class JobController {

    private static final Logger LOG = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private JobService service;


    @GetMapping("/jobs")
    public ResponseEntity<List<JobDto>> getAllJobs() {
        LOG.info("Received request for all jobs.");
        return new ResponseEntity<>(service.getAllJobs(), HttpStatus.OK);
    }
}
