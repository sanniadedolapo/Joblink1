package com.niit.JobLink;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("SingleApiSource") // Give it a unique Qualifier label!
public class ApiJobService implements JobService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<String> getAvailableJobs() {
        List<String> combinedJobs = new ArrayList<>();
        
        try {
            // 1. Target the Live Himalayas Public API
            String url = "https://himalayas.app/jobs/api";
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && response.containsKey("jobs")) {
                List<Map<String, Object>> jobsList = (List<Map<String, Object>>) response.get("jobs");
                
                // Pull the top 5 live listings to keep the dashboard fast
                for (int i = 0; i < Math.min(jobsList.size(), 5); i++) {
                    Map<String, Object> job = jobsList.get(i);
                    String display = job.get("title") + " @ " + job.get("companyName") + " (" + job.get("locationRestrictions") + ")";
                    combinedJobs.add(display);
                }
            }
        } catch (Exception e) {
            // Fallback gracefully if the external API hits a network glitch
            combinedJobs.add("Error fetching live jobs - Network Timeout");
        }
        
        return combinedJobs;
    }
}