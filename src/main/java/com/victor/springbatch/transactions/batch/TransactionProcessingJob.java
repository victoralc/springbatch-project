//package com.victor.springbatch.transactions.batch;
//
//import com.victor.springbatch.transactions.domain.AccountSummary;
//import com.victor.springbatch.transactions.domain.Transaction;
//import com.victor.springbatch.transactions.processor.TransactionApplierProcessor;
//import com.victor.springbatch.transactions.reader.TransactionReader;
//import com.victor.springbatch.transactions.repository.TransactionDao;
//import com.victor.springbatch.transactions.repository.TransactionDaoSupport;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
//import org.springframework.batch.item.database.JdbcBatchItemWriter;
//import org.springframework.batch.item.database.JdbcCursorItemReader;
//import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
//import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.FlatFileItemWriter;
//import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
//import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
//import org.springframework.batch.item.file.mapping.PassThroughFieldSetMapper;
//import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
//import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
//import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
//import org.springframework.batch.item.file.transform.FieldSet;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.io.Resource;
//
//import javax.sql.DataSource;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
////@EnableBatchProcessing
////@SpringBootApplication
//public class TransactionProcessingJob {
//
//    @Autowired
//    private JobBuilderFactory jobBuilderFactory;
//
//    @Autowired
//    private StepBuilderFactory stepBuilderFactory;
//
//    @Bean
//    @StepScope
//    public TransactionReader transactionReader() {
//        return new TransactionReader(fileItemReader());
//    }
//
//    @Bean
//    @StepScope
//    public FlatFileItemReader<FieldSet> fileItemReader() {
//        return new FlatFileItemReaderBuilder<FieldSet>()
//                .name("fileItemReader")
//                .resource(inputFile)
//                .lineTokenizer(new DelimitedLineTokenizer())
//                .fieldSetMapper(new PassThroughFieldSetMapper())
//                .build();
//    }
//
//    @Bean
//    public JdbcBatchItemWriter<Transaction> transactionWriter(DataSource dataSource) {
//        return new JdbcBatchItemWriterBuilder<Transaction>()
//                .itemSqlParameterSourceProvider(
//                        new BeanPropertyItemSqlParameterSourceProvider<>())
//                .sql("INSERT INTO TRANSACTION " +
//                        "(ACCOUNT_SUMMARY_ID, TIMESTAMP, AMOUNT) " +
//                        "VALUES ((SELECT ID FROM ACCOUNT_SUMMARY " +
//                        "	WHERE ACCOUNT_NUMBER = :accountNumber), " +
//                        ":timestamp, :amount)")
//                .dataSource(dataSource)
//                .build();
//    }
//
//    @Bean
//    public Step importTransactionFileStep() {
//        return this.stepBuilderFactory.get("importTransactionFileStep")
//                .<Transaction, Transaction>chunk(100)
//                .reader(transactionReader())
//                .writer(transactionWriter(null))
//                .allowStartIfComplete(true)
//                .listener(transactionReader())
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    public JdbcCursorItemReader<AccountSummary> accountSummaryReader(DataSource dataSource) {
//        return new JdbcCursorItemReaderBuilder<AccountSummary>()
//                .name("accountSummaryReader")
//                .dataSource(dataSource)
//                .sql("SELECT ACCOUNT_NUMBER, CURRENT_BALANCE " +
//                        "FROM ACCOUNT_SUMMARY A " +
//                        "WHERE A.ID IN (" +
//                        "	SELECT DISTINCT T.ACCOUNT_SUMMARY_ID " +
//                        "	FROM TRANSACTION T) " +
//                        "ORDER BY A.ACCOUNT_NUMBER")
//                .rowMapper((resultSet, rowNumber) -> {
//                    AccountSummary summary = new AccountSummary();
//
//                    summary.setAccountNumber(resultSet.getString("account_number"));
//                    summary.setCurrentBalance(resultSet.getDouble("current_balance"));
//
//                    return summary;
//                }).build();
//    }
//
//    @Bean
//    public TransactionDao transactionDao(DataSource dataSource) {
//        return new TransactionDaoSupport(dataSource);
//    }
//
//    @Bean
//    public TransactionApplierProcessor transactionApplierProcessor() {
//        return new TransactionApplierProcessor(transactionDao(null));
//    }
//
//    @Bean
//    public JdbcBatchItemWriter<AccountSummary> accountSummaryWriter(DataSource dataSource) {
//        return new JdbcBatchItemWriterBuilder<AccountSummary>()
//                .dataSource(dataSource)
//                .itemSqlParameterSourceProvider(
//                        new BeanPropertyItemSqlParameterSourceProvider<>())
//                .sql("UPDATE ACCOUNT_SUMMARY " +
//                        "SET CURRENT_BALANCE = :currentBalance " +
//                        "WHERE ACCOUNT_NUMBER = :accountNumber")
//                .build();
//    }
//
//    @Bean
//    public Step applyTransactionsStep() {
//        return this.stepBuilderFactory.get("applyTransactionsStep")
//                .<AccountSummary, AccountSummary>chunk(100)
//                .reader(accountSummaryReader(null))
//                .processor(transactionApplierProcessor())
//                .writer(accountSummaryWriter(null))
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    public FlatFileItemWriter<AccountSummary> accountSummaryFileWriter() {
//
//        DelimitedLineAggregator<AccountSummary> lineAggregator =
//                new DelimitedLineAggregator<>();
//        BeanWrapperFieldExtractor<AccountSummary> fieldExtractor =
//                new BeanWrapperFieldExtractor<>();
//        fieldExtractor.setNames(new String[] {"accountNumber", "currentBalance"});
//        fieldExtractor.afterPropertiesSet();
//        lineAggregator.setFieldExtractor(fieldExtractor);
//
//        return new FlatFileItemWriterBuilder<AccountSummary>()
//                .name("accountSummaryFileWriter")
//                .resource(summaryFile)
//                .lineAggregator(lineAggregator)
//                .build();
//    }
//
//    @Bean
//    public Step generateAccountSummaryStep() {
//        return this.stepBuilderFactory.get("generateAccountSummaryStep")
//                .<AccountSummary, AccountSummary>chunk(100)
//                .reader(accountSummaryReader(null))
//                .writer(accountSummaryFileWriter())
//                .build();
//    }
//
//    @Bean
//    public Job transactionJob() {
//        return this.jobBuilderFactory.get("transactionJob")
//                .preventRestart()
//                .start(importTransactionFileStep())
//                .next(applyTransactionsStep())
//                .next(generateAccountSummaryStep())
//                .build();
////		return this.jobBuilderFactory.get("transactionJob")
////				.start(importTransactionFileStep())
////				.on("STOPPED").stopAndRestart(importTransactionFileStep())
////				.from(importTransactionFileStep()).on("*").to(applyTransactionsStep())
////				.from(applyTransactionsStep()).next(generateAccountSummaryStep())
////				.end()
////				.build();
//    }
//
//    @Value("file:///dev/files/batch/summaryFile.csv")
//    Resource summaryFile;
//
//    @Value("classpath:input/transactionFile.csv")
//    Resource inputFile;
//
//    public static void main(String[] args) {
//        SpringApplication.run(TransactionProcessingJob.class, args);
//    }
//}
