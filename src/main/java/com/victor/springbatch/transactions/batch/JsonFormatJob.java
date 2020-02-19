package com.victor.springbatch.transactions.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victor.springbatch.transactions.domain.Customer;
import com.victor.springbatch.transactions.domain.Transaction;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.text.SimpleDateFormat;

@EnableBatchProcessing
@SpringBootApplication
public class JsonFormatJob {

    @Value("classpath:input/customer.json")
    Resource inputFile;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    public static void main(String[] args) {
        SpringApplication.run(JsonFormatJob.class, args);
    }

    @Bean
	@StepScope
	public JsonItemReader<Customer> customerFileReader() {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));

		JacksonJsonObjectReader<Customer> jsonObjectReader = new JacksonJsonObjectReader<>(Customer.class);
		jsonObjectReader.setMapper(objectMapper);

		return new JsonItemReaderBuilder<Customer>()
				.name("customerFileReader")
				.jsonObjectReader(jsonObjectReader)
				.resource(inputFile)
				.build();
	}

    @Bean
    public Jaxb2Marshaller customerMarshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();

        jaxb2Marshaller.setClassesToBeBound(Customer.class,
                Transaction.class);

        return jaxb2Marshaller;
    }

    @Bean
    public ItemWriter xmlCustomerFileItemWriter() {
        return (items) -> items.forEach(System.out::println);
    }

    @Bean
    public Step xmlCustomerFileStep() {
        return this.stepBuilderFactory.get("xmlCustomerFileStep")
                .<Customer, Customer>chunk(10)
                .reader(customerFileReader())
                .writer(xmlCustomerFileItemWriter())
                .build();
    }

    @Bean
    public Job xmlCustomerFileJob() {
        return this.jobBuilderFactory.get("xmlCustomerFileJob")
                .start(xmlCustomerFileStep())
                .build();
    }

}
