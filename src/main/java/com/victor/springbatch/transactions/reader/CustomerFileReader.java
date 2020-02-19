package com.victor.springbatch.transactions.reader;

import com.victor.springbatch.transactions.domain.Customer;
import com.victor.springbatch.transactions.domain.Transaction;
import org.springframework.batch.item.*;

import java.util.ArrayList;

public class CustomerFileReader implements ItemStreamReader<Customer> {

    private Object currItem = null;

    private ItemStreamReader<Object> delegate;

    public CustomerFileReader(ItemStreamReader<Object> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Customer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (currItem == null) {
            this.currItem = delegate.read();
        }

        Customer item = (Customer) currItem;
        currItem = null;

        if (item != null){
            item.setTransactions(new ArrayList<>());

            while(peek() instanceof Transaction){
                item.getTransactions().add((Transaction) currItem);
                currItem = null;
            }
        }
        return item;
    }

    private Object peek() throws Exception {
        if (currItem == null){
            currItem = delegate.read();
        }
        return currItem;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.delegate.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        this.delegate.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        this.delegate.close();
    }
}
