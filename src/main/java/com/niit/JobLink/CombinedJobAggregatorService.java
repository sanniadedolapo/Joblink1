package com.niit.JobLink;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("AggregatedApiSource")
public class CombinedJobAggregatorService implements JobService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<String> getAvailableJobs() {
        List<String> masterJobList = new ArrayList<>();

        // === API 1: Live Himalayas API (Remote/Tech Tech Jobs) ===
        try {
            String url = "https://himalayas.app/jobs/api";
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("jobs")) {
                List<Map<String, Object>> jobsList = (List<Map<String, Object>>) response.get("jobs");
                for (int i = 0; i < Math.min(jobsList.size(), 3); i++) {
                    Map<String, Object> job = jobsList.get(i);
                    masterJobList.add("[Himalayas Remote] " + job.get("title") + " @ " + job.get("companyName"));
                }
            }
        } catch (Exception e) {
            masterJobList.add("[Himalayas API] Outage - Falling back gracefully");
        }

        // === API 2: Live Greenhouse Public Board (GitHub Corporate Openings) ===
        try {
            String url = "https://boards-api.greenhouse.io/v1/boards/github/jobs";
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("jobs")) {
                List<Map<String, Object>> jobsList = (List<Map<String, Object>>) response.get("jobs");
                for (int i = 0; i < Math.min(jobsList.size(), 3); i++) {
                    Map<String, Object> job = jobsList.get(i);
                    masterJobList.add("[GitHub Corporate] " + job.get("title") + " (" + job.get("location") + ")");
                }
            }
        } catch (Exception e) {
            masterJobList.add("[GitHub Greenhouse API] Rate-limited or Down");
        }

        // === API 3: Live Arbeitnow API (European & Remote Tech Openings) ===
        try {
            String url = "https://www.arbeitnow.com/api/job-board-api";
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("data")) {
                List<Map<String, Object>> jobsList = (List<Map<String, Object>>) response.get("data");
                for (int i = 0; i < Math.min(jobsList.size(), 3); i++) {
                    Map<String, Object> job = jobsList.get(i);
                    masterJobList.add("[Arbeitnow Tech] " + job.get("title") + " @ " + job.get("company_name"));
                }
            }
        } catch (Exception e) {
            masterJobList.add("[Arbeitnow API] Pipeline unavailable");
        }

        // Ultimate safety net fallback if all network interfaces drop out locally
        if (masterJobList.isEmpty() || masterJobList.stream().allMatch(s -> s.contains("API]"))) {
            masterJobList.add("[Cache Backup] Java Software Engineer - Lagos Enterprise Hub");
            masterJobList.add("[Cache Backup] Cloud DevOps Infrastructure Consultant - Ikeja");
        }

        return masterJobList;
    }
}