package com.victor.springbatch.transactions.repository;

import com.victor.springbatch.transactions.domain.AccountSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

public interface AccountSummaryRepository extends JpaRepository<AccountSummary, Long> {


}
