package com.kira.jobms.job;

import com.kira.jobms.job.Job;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping
public class JobController {

    private final com.kira.jobms.job.JobService jobService;

    public JobController(com.kira.jobms.job.JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<com.kira.jobms.job.Job>> findAll() {
        return ResponseEntity.ok(jobService.findAll());
    }

    @PostMapping("/jobs")
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        Job createdJob = jobService.createJob(job);
        URI location = URI.create("/jobs/" + createdJob.getId());
        return ResponseEntity.created(location).body(createdJob);
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<Job> findById(@PathVariable Long id) {
        Job job = jobService.findById(id);
        if (job != null) {
            return ResponseEntity.ok(job);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok("Job deleted successfully!");
    }

    @PutMapping("/jobs/{id}")
    public ResponseEntity<String> updateJob(@PathVariable Long id, @RequestBody Job updatedJob) {
        boolean updated = jobService.updateJob(id, updatedJob);
        if (updated) {
            return ResponseEntity.ok("Job updated successfully!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
