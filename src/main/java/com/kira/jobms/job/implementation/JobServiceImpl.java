package com.kira.jobms.job.implementation;


import com.kira.jobms.job.Job;
import com.kira.jobms.job.JobRepository;
import com.kira.jobms.job.JobService;
import com.kira.jobms.job.clients.CompanyClient;
import com.kira.jobms.job.dto.JobWithCompanyDTO;
import com.kira.jobms.job.external.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
public class JobServiceImpl implements JobService {
    // This class should implement the methods defined in the jobService interface.
    // For example, you might have a list of jobs and methods to add or retrieve them.

    // Constructor injection of the JobRepository
    private JobRepository jobRepository;

    @Autowired
    private RestTemplate restTemplate;

    private CompanyClient companyClient;

    public JobServiceImpl(JobRepository jobRepository , RestTemplate restTemplate , CompanyClient companyClient) {
        this.jobRepository = jobRepository;
        this.restTemplate = restTemplate;
        this.companyClient = companyClient;

    }




    @Override
    public List<JobWithCompanyDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        List<JobWithCompanyDTO> jobWithCompanyDTOS = new ArrayList<>();
        for (Job job : jobs) {
            jobWithCompanyDTOS.add(createJobWithCompanyDTO(job));
        }
        return jobWithCompanyDTOS;
    }

    private JobWithCompanyDTO createJobWithCompanyDTO(Job job) {
        JobWithCompanyDTO dto = new JobWithCompanyDTO();
        dto.setJob(job);

        String companyUrl = "http://localhost:8086/companies/" + job.getCompanyId();
        try {
            Company company = companyClient.getCompanyById(job.getCompanyId());
            dto.setCompany(company != null ? company : new Company());
        } catch (Exception e) {
            // log error and assign empty company
            System.err.println("Error fetching company for job ID " + job.getId() + ": " + e.getMessage());
            dto.setCompany(new Company());
        }
        return dto;
    }
    @Override
    public Job createJob(Job job) {
        // Implementation to create a new job
        // Add the job to the list or database
        jobRepository.save(job); // Save the job using the repository
        return job;
    }
    @Override
    public JobWithCompanyDTO findById(Long id){
        Job job =  jobRepository.findById(id).orElse(null); // Find a job by ID, return null if not found
        return createJobWithCompanyDTO(job);
    }
    @Override
    public boolean deleteJob(Long id) {
        // Implementation to delete a job by ID
        try {
            jobRepository.deleteById((long) id); // Delete the job using the repository
            return true;
        }
        catch (Exception e){
            // Handle the exception if the job with the given ID does not exist
            System.out.println("Job with ID " + id + " not found.");
            return false; // Return false if deletion was unsuccessful
        }
    }
    @Override
    public boolean updateJob(long id, Job updatedJob) {
        return jobRepository.findById(id)
                .map(existingJob -> {
                    updatedJob.setId(id);
                    jobRepository.save(updatedJob);
                    return true;
                })
                .orElseGet(() -> {
                    System.out.println("Job with ID " + id + " not found.");
                    return false;
                });
    }

}
