package com.victor.springbatch.transactions.domain;

import java.util.Date;

public class Transaction {

    private String accountNumber;
    private Date timestamp;
    private Double amount;

    public Transaction() {
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "accountNumber='" + accountNumber + '\'' +
                ", timestamp=" + timestamp +
                ", amount=" + amount +
                '}';
    }
}
