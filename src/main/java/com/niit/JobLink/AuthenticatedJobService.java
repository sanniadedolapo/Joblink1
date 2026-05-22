package com.niit.JobLink;

import org.springframework.stereotype.Service;
import java.util.List;

@Service("RealJobSource")//Defining this as the specific bean
public class AuthenticatedJobService implements JobService{

   @Override
   public List<String> getAvailableJobs(){
     return List.of("Java Developer - Lagos", "System Analyst - Ikeja", "IT Instructor - Surulere");
  }
}