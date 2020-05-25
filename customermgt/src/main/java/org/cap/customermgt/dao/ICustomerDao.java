package org.cap.customermgt.dao;

import org.cap.customermgt.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICustomerDao extends JpaRepository<Customer,Integer> {

    Customer findByUsername(String username) ;
}
