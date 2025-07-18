package com.kira.jobms.job;

import com.kira.jobms.job.Job;
import com.kira.jobms.job.dto.JobWithCompanyDTO;
import com.kira.jobms.job.external.Company;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<List<JobWithCompanyDTO>> findAll() {
        return ResponseEntity.ok(jobService.findAll());
    }

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        Job createdJob = jobService.createJob(job);
        URI location = URI.create("/jobs/" + createdJob.getId());
        return ResponseEntity.created(location).body(createdJob);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobWithCompanyDTO> findById(@PathVariable Long id) {
        JobWithCompanyDTO job = jobService.findById(id);
        if (job != null) {
            return ResponseEntity.ok(job);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok("Job deleted successfully!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateJob(@PathVariable Long id, @RequestBody Job updatedJob) {
        boolean updated = jobService.updateJob(id, updatedJob);
        if (updated) {
            return ResponseEntity.ok("Job updated successfully!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
