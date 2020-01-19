package com.victor.springbatch.transactions.writer;

import com.victor.springbatch.transactions.domain.AccountSummary;
import com.victor.springbatch.transactions.repository.AccountSummaryRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AccountSummaryWriter implements ItemWriter<AccountSummary> {

    private AccountSummaryRepository repository;

    public AccountSummaryWriter(AccountSummaryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(List<? extends AccountSummary> items) throws Exception {
        items.forEach(accountSummary -> repository.save(accountSummary));
    }
}
