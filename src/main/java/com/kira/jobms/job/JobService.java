package com.kira.jobms.job;

import java.util.List;

public interface JobService {
    List<Job> findAll();
    Job createJob(Job job);
    Job findById(Long id);
    boolean deleteJob(Long id);
    boolean updateJob(long id ,Job job); // Method to update a job
}
