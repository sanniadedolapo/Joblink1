package com.niit.JobLink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService localService;
    private final JobService singleApiService;
    private final JobService multipleApiService;

    // Directing Spring to wire up all 3 strategies concurrently
    @Autowired
    public JobController(
            @Qualifier("RealJobSource") JobService localService,
            @Qualifier("SingleApiSource") JobService singleApiService,
            @Qualifier("AggregatedApiSource") JobService multipleApiService) {
        this.localService = localService;
        this.singleApiService = singleApiService;
        this.multipleApiService = multipleApiService;
    }

    // Endpoint 1: Local Mock Database
    // URL: http://localhost:8080/jobs/local
    @GetMapping("/local")
    public String showLocalJobs() {
        return buildDashboard("In-Memory Local Feed", localService.getAvailableJobs(), "Variant 1: Local Mocking");
    }

    // Endpoint 2: Single Live Cloud API Connection
    // URL: http://localhost:8080/jobs/single
    @GetMapping("/single")
    public String showSingleApiJobs() {
        return buildDashboard("Single Cloud Live Feed", singleApiService.getAvailableJobs(), "Variant 2: Pure REST Client");
    }

    // Endpoint 3: Multi-API Dynamic Aggregation
    // URL: http://localhost:8080/jobs/multiple
    @GetMapping("/multiple")
    public String showMultipleApiJobs() {
        return buildDashboard("Distributed Multi-API Cloud Aggregator", multipleApiService.getAvailableJobs(), "Variant 3: Distributed Network Aggregation");
    }

    // Unified Frontend UI Engine
    private String buildDashboard(String title, List<String> jobs, String footprint) {
        StringBuilder listHtml = new StringBuilder();
        for (String job : jobs) {
            listHtml.append("<li style='padding:12px; border-bottom:1px solid #eee; color:#2c3e50; font-size:14px; font-weight:500;'>")
                    .append(job)
                    .append("</li>");
        }

        return "<html><body style='font-family:sans-serif; background:#f4f6f9; display:flex; justify-content:center; padding:50px;'>" +
               "<div style='background:white; width:550px; padding:35px; border-radius:16px; box-shadow:0 12px 24px rgba(0,0,0,0.08);'>" +
               "<h2 style='color:#0d47a1; border-bottom:4px solid #0d47a1; padding-bottom:12px; margin-top:0; font-size:22px;'>" + title + "</h2>" +
               "<ul style='padding:0; list-style:none; margin:20px 0;'>" + listHtml.toString() + "</ul>" +
               "<div style='background:#e3f2fd; padding:10px; border-radius:8px; text-align:center;'>" +
               "<p style='font-size:11px; color:#1565c0; margin:0; font-weight:bold;'>" + footprint + " • Module 5 Core Architecture</p>" +
               "</div>" +
               "</div></body></html>";
    }
}