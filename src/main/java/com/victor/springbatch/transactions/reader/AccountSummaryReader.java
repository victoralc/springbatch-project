package com.victor.springbatch.transactions.reader;

import com.victor.springbatch.transactions.domain.AccountSummary;
import com.victor.springbatch.transactions.repository.AccountSummaryRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountSummaryReader implements ItemReader<AccountSummary> {

    @Autowired
    private AccountSummaryRepository repository;

    @Override
    public AccountSummary read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        AccountSummary accountSummary = new AccountSummary();
        accountSummary.setAccountNumber("3985729387");
        accountSummary.setCurrentBalance(497.72);
        return accountSummary;
    }
}
