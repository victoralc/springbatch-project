package com.victor.springbatch.transactions.mappers;

import com.victor.springbatch.transactions.domain.Customer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CustomerFieldSetMapper implements org.springframework.batch.item.file.mapping.FieldSetMapper<Customer> {
    @Override
    public Customer mapFieldSet(FieldSet fieldSet) throws BindException{
        Customer customer = new Customer();
        customer.setAddress(fieldSet.readString("addressNumber") + " " + fieldSet.readString("street"));
        customer.setFirstName(fieldSet.readString("firstName"));
        customer.setLastName(fieldSet.readString("lastName"));
        customer.setMiddleInitial(fieldSet.readString("middleInitial"));
        customer.setState(fieldSet.readString("state"));
        customer.setZipCode(fieldSet.readString("zipCode"));
        customer.setCity(fieldSet.readString("city"));
        return customer;
    }
}
