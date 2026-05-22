package com.niit.flightsystem;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.List;

@RestController // Note: Use @Controller for UI, not @RestController
public class FlightController {

    //@RestController
    @RequestMapping("/flight")
    public String viewDashboard(Model model) {
        // Mock data to simulate our Flight system
        model.addAttribute("username", "Instructor Wisdom");
        model.addAttribute("systemStatus", "Operational");
        
        List<String> flights = Arrays.asList("NIIT-101 (Lagos)", "NIIT-202 (London)", "NIIT-303 (Dubai)");
        model.addAttribute("activeFlights", flights);

        return "index"; // This looks for index.html in the templates folder
    }
}