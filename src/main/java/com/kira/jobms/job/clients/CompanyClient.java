package com.kira.jobms.job.clients;

import com.kira.jobms.job.external.Company;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-service", url = "http://localhost:8086")
public interface CompanyClient {
    @GetMapping("/companies/{id}")
    Company getCompanyById(@PathVariable("id") Long id);
}
