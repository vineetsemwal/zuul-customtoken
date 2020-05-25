package org.cap.customermgt.service;

import org.cap.customermgt.entities.Customer;

import java.util.List;

public interface ICustomerService {
    Customer findById(int id);

    Customer findByUsername(String username);

    List<Customer> fetchAll();

    Customer save(Customer customer);

    boolean credentialsCorrect(int id, String password);


    void remove(int id);
}
