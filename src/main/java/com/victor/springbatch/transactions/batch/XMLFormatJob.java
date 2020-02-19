//package com.victor.springbatch.transactions.batch;
//
//import com.victor.springbatch.transactions.domain.Customer;
//import com.victor.springbatch.transactions.domain.Transaction;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
//import org.springframework.batch.item.xml.StaxEventItemReader;
//import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.io.Resource;
//import org.springframework.oxm.jaxb.Jaxb2Marshaller;
//
//@EnableBatchProcessing
//@SpringBootApplication
//public class XMLFormatJob {
//
//    @Value("classpath:input/customers.xml")
//    Resource inputFile;
//
//    @Autowired
//    private JobBuilderFactory jobBuilderFactory;
//
//    @Autowired
//    private StepBuilderFactory stepBuilderFactory;
//
//    public static void main(String[] args) {
//        SpringApplication.run(XMLFormatJob.class, args);
//    }
//
//    @Bean
//	@StepScope
//	public StaxEventItemReader<Customer> customerFileReader() {
//		return new StaxEventItemReaderBuilder<Customer>()
//				.name("customerFileReader")
//				.resource(inputFile)
//				.addFragmentRootElements("customer")
//				.unmarshaller(customerMarshaller())
//				.build();
//	}
//
//    @Bean
//	public Jaxb2Marshaller customerMarshaller() {
//		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
//
//		jaxb2Marshaller.setClassesToBeBound(Customer.class,
//				Transaction.class);
//
//		return jaxb2Marshaller;
//	}
//
//    @Bean
//    public ItemWriter xmlCustomerFileItemWriter() {
//        return (items) -> items.forEach(System.out::println);
//    }
//
//    @Bean
//    public Step xmlCustomerFileStep() {
//        return this.stepBuilderFactory.get("xmlCustomerFileStep")
//                .<Customer, Customer>chunk(10)
//                .reader(customerFileReader())
//                .writer(xmlCustomerFileItemWriter())
//                .build();
//    }
//
//    @Bean
//    public Job xmlCustomerFileJob() {
//        return this.jobBuilderFactory.get("xmlCustomerFileJob")
//                .start(xmlCustomerFileStep())
//                .build();
//    }
//
//}
