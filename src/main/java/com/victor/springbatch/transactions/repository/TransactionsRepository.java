package com.victor.springbatch.transactions.repository;

import com.victor.springbatch.transactions.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionsRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountNumber(String accountNumber);
}
