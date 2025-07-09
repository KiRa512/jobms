package com.kira.jobms.job.implementation;


import com.kira.jobms.job.Job;
import com.kira.jobms.job.JobRepository;
import com.kira.jobms.job.JobService;
import com.kira.jobms.job.dto.JobWithCompanyDTO;
import com.kira.jobms.job.external.Company;
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

    public JobServiceImpl(JobRepository jobRepository){
        this.jobRepository = jobRepository;
    }

    @Override
    public List<JobWithCompanyDTO> findAll() {
        // Implementation to return all jobs
        List <Job> jobs = jobRepository.findAll();
        List<JobWithCompanyDTO> jobWithCompanyDTOS = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        for(Job job: jobs){
            JobWithCompanyDTO jobWithCompanyDTO = new JobWithCompanyDTO();
            jobWithCompanyDTO.setJob(job);
            String companyUrl = "http://localhost:8086/companies/" + job.getCompanyId();
            try{
                Company company = restTemplate.getForObject(companyUrl, Company.class);
                if (company != null) {
                    jobWithCompanyDTO.setCompany(company);
                } else {
                    jobWithCompanyDTO.setCompany(new Company());
                }

            }
            catch (Exception e) {
                jobWithCompanyDTO.setCompany(new Company());
            }
            jobWithCompanyDTOS.add(jobWithCompanyDTO);

        }

        return jobWithCompanyDTOS;

    }

    @Override
    public Job createJob(Job job) {
        // Implementation to create a new job
        // Add the job to the list or database
        jobRepository.save(job); // Save the job using the repository
        return job;
    }
    @Override
    public Job findById(Long id){
        return jobRepository.findById(id).orElse(null); // Find a job by ID, return null if not found
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
