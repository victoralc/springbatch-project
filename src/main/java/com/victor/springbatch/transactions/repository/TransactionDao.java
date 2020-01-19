package com.victor.springbatch.transactions.repository;

import com.victor.springbatch.transactions.domain.Transaction;

import java.util.List;

public interface TransactionDao {

    List<Transaction> getTransactionsByAccountNumber(String accountNumber);

}
