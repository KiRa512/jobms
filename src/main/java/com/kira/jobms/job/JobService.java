package com.kira.jobms.job;

import com.kira.jobms.job.dto.JobWithCompanyDTO;

import java.util.List;

public interface JobService {
    List<JobWithCompanyDTO> findAll();
    Job createJob(Job job);
    JobWithCompanyDTO findById(Long id);
    boolean deleteJob(Long id);
    boolean updateJob(long id ,Job job); // Method to update a job
}
