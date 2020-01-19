package com.victor.springbatch.transactions;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobLauncherController {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private JobExplorer jobExplorer;

    @PostMapping("/run")
    public ExitStatus runJob(@RequestBody JobLaunchRequest jobLaunchRequest) throws Exception {
        Job job = this.context.getBean(jobLaunchRequest.getName(), Job.class);
        JobParameters jobParameters = new JobParametersBuilder(jobLaunchRequest.getJobParameters(), this.jobExplorer)
                .getNextJobParameters(job)
                .toJobParameters();
        return this.jobLauncher.run(job, jobParameters).getExitStatus();
    }
}
