package org.cap.customermgt.controller;

import org.cap.customermgt.dto.AddCustomerRequestData;
import org.cap.customermgt.exceptions.UnAuthorizedException;
import org.cap.customermgt.service.ICustomerService;
import org.cap.customermgt.entities.Customer;
import org.cap.customermgt.exceptions.CustomerNotFoundException;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/customers")
public class CustomersController {
    private static final Logger Log = LoggerFactory.getLogger(CustomersController.class);

    @Autowired
    private ICustomerService service;


    /**
     * final url will be /customers/find/{id}
     *
     * Case when only user can fetch his information, not of any other user
     * Admin can fetch information of other users too
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<Customer> findById(@PathVariable("id") int id, HttpServletRequest request) {
        String requestSenderUsername= request.getHeader("requestsender");
        Customer requestSender=service.findByUsername(requestSenderUsername);
        if(requestSender.getId()==id || "admin".equals(requestSender.getRole()))  {
            Customer desiredUser=service.findById(id);
            ResponseEntity<Customer> response = new ResponseEntity<>(desiredUser, HttpStatus.OK);
            return response;
        }
        throw new UnAuthorizedException("you are not authorized");
    }


    @PostMapping("/add")
    public ResponseEntity<Customer> addCustomer(@RequestBody AddCustomerRequestData dto) {
        Customer customer = convert(dto);
        customer=service.save(customer);
        ResponseEntity<Customer> response = new ResponseEntity<>(customer, HttpStatus.OK);
        return response;
    }

    public Customer convert(AddCustomerRequestData dto) {
        Customer customer = new Customer();
        customer.setUsername(dto.getUsername());
        customer.setPassword(dto.getPassword());
        customer.setRole(dto.getRole());
        return customer;
    }

}
