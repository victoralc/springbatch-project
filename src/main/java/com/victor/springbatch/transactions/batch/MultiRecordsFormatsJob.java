//package com.victor.springbatch.transactions.batch;
//
//import com.victor.springbatch.transactions.domain.Customer;
//import com.victor.springbatch.transactions.mappers.TransactionFieldSetMapper;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
//import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
//import org.springframework.batch.item.file.mapping.FieldSetMapper;
//import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;
//import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
//import org.springframework.batch.item.file.transform.LineTokenizer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.io.Resource;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@EnableBatchProcessing
//@SpringBootApplication
//public class MultiRecordsFormatsJob {
//
//    @Autowired
//    private JobBuilderFactory jobBuilderFactory;
//
//    @Autowired
//    private StepBuilderFactory stepBuilderFactory;
//
//    @Value("classpath:input/multiFormatsFile")
//    Resource inputFile;
//
//    @Bean
//    @StepScope
//    public FlatFileItemReader<Customer> multiRecordsCustomerFileItemReader() {
//
//        return new FlatFileItemReaderBuilder<Customer>()
//                .name("multiRecordsCustomerFileItemReader")
//                .lineMapper(multiFormatLineMapper())
//                .resource(inputFile)
//                .build();
//    }
//
//    @Bean
//	public PatternMatchingCompositeLineMapper multiFormatLineMapper() {
//        Map<String, LineTokenizer> lineTokenizers =
//                new HashMap<>(2);
//
//        lineTokenizers.put("CUST*", customerLineTokenizer());
//        lineTokenizers.put("TRANS*", transactionLineTokenizer());
//
//        Map<String, FieldSetMapper> fieldSetMappers =
//                new HashMap<>(2);
//
//        BeanWrapperFieldSetMapper<Customer> customerFieldSetMapper =
//                new BeanWrapperFieldSetMapper<>();
//        customerFieldSetMapper.setTargetType(Customer.class);
//
//        fieldSetMappers.put("CUST*", customerFieldSetMapper);
//        fieldSetMappers.put("TRANS*", new TransactionFieldSetMapper());
//
//        PatternMatchingCompositeLineMapper lineMappers =
//                new PatternMatchingCompositeLineMapper();
//
//        lineMappers.setTokenizers(lineTokenizers);
//        lineMappers.setFieldSetMappers(fieldSetMappers);
//
//        return lineMappers;
//
//    }
//
//    @Bean
//	public DelimitedLineTokenizer transactionLineTokenizer() {
//		DelimitedLineTokenizer lineTokenizer =
//				new DelimitedLineTokenizer();
//
//		lineTokenizer.setNames("prefix",
//				"accountNumber",
//				"timestamp",
//				"amount");
//
//		return lineTokenizer;
//	}
//
//	@Bean
//	public DelimitedLineTokenizer customerLineTokenizer() {
//		DelimitedLineTokenizer lineTokenizer =
//				new DelimitedLineTokenizer();
//
//		lineTokenizer.setNames("firstName",
//				"middleInitial",
//				"lastName",
//				"address",
//				"city",
//				"state",
//				"zipCode");
//
//		lineTokenizer.setIncludedFields(1, 2, 3, 4, 5, 6, 7);
//
//		return lineTokenizer;
//	}
//
//    @Bean
//    public ItemWriter multiRecordsFileItemWriter() {
//        return (items) -> items.forEach(System.out::println);
//    }
//
//    @Bean
//    public Step multiRecordsFileCustomerStep() {
//        return this.stepBuilderFactory.get("multiRecordsFileCustomerStep")
//                .<Customer, Customer>chunk(10)
//                .reader(multiRecordsCustomerFileItemReader())
//                .writer(multiRecordsFileItemWriter())
//                .build();
//    }
//
//    @Bean
//    public Job multiRecordsFileJob() {
//        return this.jobBuilderFactory.get("multiRecordsFileJob")
//                .start(multiRecordsFileCustomerStep())
//                .build();
//    }
//
//    public static void main(String[] args) {
//        SpringApplication.run(MultiRecordsFormatsJob.class, args);
//    }
//
//}
