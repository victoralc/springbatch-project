package com.victor.springbatch.transactions.writer;

import com.victor.springbatch.transactions.domain.Transaction;
import com.victor.springbatch.transactions.repository.TransactionsRepository;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class TransactionsItemWriter implements ItemWriter<Transaction> {

    private TransactionsRepository repository;

    public TransactionsItemWriter(TransactionsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(List<? extends Transaction> items) throws Exception {
        repository.saveAll(items);
    }

}
