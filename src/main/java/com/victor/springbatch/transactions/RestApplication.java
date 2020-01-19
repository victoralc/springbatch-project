package com.victor.springbatch.transactions;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootApplication
public class RestApplication {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("job").incrementer(new RunIdIncrementer()).start(step1()).build();
    }

    @Bean
    public Step step1() {
        return this.stepBuilderFactory.get("step1").tasklet((stepContribution, chunkContext) -> {
            System.out.println("running step1 via rest application");
            System.out.println("Job Parameters: " + stepContribution.getStepExecution().getJobParameters());
            return RepeatStatus.FINISHED;
        }).build();
    }

    public static void main(String[] args) {
        new SpringApplication(RestApplication.class).run(args);
    }


}
