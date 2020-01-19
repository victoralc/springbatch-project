package com.victor.springbatch.transactions.batch;

import com.victor.springbatch.transactions.domain.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import java.util.Arrays;
import java.util.List;

@EnableBatchProcessing
@SpringBootApplication
public class DelimitedFileJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Value("classpath:input/customerFile.csv")
    Resource inputFile;

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> delimitedCustomerFileItemReader() {

        return new FlatFileItemReaderBuilder<Customer>()
                .name("delimitedCustomerFileItemReader")
                .resource(inputFile)
                .delimited()
                .names(new String[] {"firstName", "middleInitial", "lastName",
                        "addressNumber", "street", "city", "state","zipCode"})
                .targetType(Customer.class)
                .build();
    }

    @Bean
    public ItemWriter<Customer> delimitedFileItemWriter() {
        return (items) -> items.forEach(System.out::println);
    }

    @Bean
    public Step delimitedFileCustomerStep() {
        return this.stepBuilderFactory.get("delimitedFileCustomerStep")
                .<Customer, Customer>chunk(10)
                .reader(delimitedCustomerFileItemReader())
                .writer(delimitedFileItemWriter())
                .build();
    }

    @Bean
    public Job delimitedCustomerFileJob() {
        return this.jobBuilderFactory.get("delimitedCustomerFileJob")
                .start(delimitedFileCustomerStep())
                .build();
    }


    public static void main(String[] args) {
        SpringApplication.run(DelimitedFileJob.class, args);
    }

}
