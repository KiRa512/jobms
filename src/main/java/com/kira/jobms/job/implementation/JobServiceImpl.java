package com.kira.jobms.job.implementation;


import com.kira.jobms.job.Job;
import com.kira.jobms.job.JobRepository;
import com.kira.jobms.job.JobService;
import com.kira.jobms.job.clients.CompanyClient;
import com.kira.jobms.job.dto.JobWithCompanyDTO;
import com.kira.jobms.job.external.Company;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class JobServiceImpl implements JobService {
    // This class should implement the methods defined in the jobService interface.
    // For example, you might have a list of jobs and methods to add or retrieve them.

    // Constructor injection of the JobRepository
    private JobRepository jobRepository;


    private CompanyClient companyClient;

    public JobServiceImpl(JobRepository jobRepository ,  CompanyClient companyClient) {
        this.jobRepository = jobRepository;
        this.companyClient = companyClient;

    }




    @Override
    @CircuitBreaker(name = "companyBreaker",fallbackMethod = "fallbackFindAll")
    public List<JobWithCompanyDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        List<JobWithCompanyDTO> jobWithCompanyDTOS = new ArrayList<>();
        for (Job job : jobs) {
            jobWithCompanyDTOS.add(createJobWithCompanyDTO(job));
        }
        return jobWithCompanyDTOS;
    }

    // Fallback method for findAll
    public List<JobWithCompanyDTO> fallbackFindAll(Throwable t) {
        System.err.println("Fallback triggered: " + t.getMessage());

        Job fallbackJob = new Job();
        fallbackJob.setTitle("N/A");
        fallbackJob.setDescription("Job data not available due to service disruption.");

        Company fallbackCompany = new Company();
        fallbackCompany.setName("Company Service Unavailable");
        fallbackCompany.setDescription("The company service is currently not reachable.");

        JobWithCompanyDTO fallbackDTO = new JobWithCompanyDTO();
        fallbackDTO.setJob(fallbackJob);
        fallbackDTO.setCompany(fallbackCompany);

        return List.of(fallbackDTO);
    }


    private JobWithCompanyDTO createJobWithCompanyDTO(Job job) {
        JobWithCompanyDTO dto = new JobWithCompanyDTO();
        dto.setJob(job);
        Company company = companyClient.getCompanyById(job.getCompanyId());
        dto.setCompany(company);
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
