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
import java.util.List;

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
     * Admin can fetch information too
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<Customer> findById(@PathVariable("id") int id, HttpServletRequest request) {
        String requestSenderString= request.getHeader("requestsender");
        int requestSenderId=Integer.parseInt(requestSenderString);
        Customer requestSender=service.findById(requestSenderId);
        if(requestSenderId==id || "admin".equals(requestSender.getRole()))  {
            Customer desiredUser=service.findById(id);
            ResponseEntity<Customer> response = new ResponseEntity<>(desiredUser, HttpStatus.OK);
            return response;
        }
        throw new UnAuthorizedException("you are not authorized");
    }


    @PostMapping("/add")
    public ResponseEntity<Customer> addCustomer(@RequestBody AddCustomerRequestData dto) {
        Customer customer = convert(dto);
        service.save(customer);
        ResponseEntity<Customer> response = new ResponseEntity<>(customer, HttpStatus.OK);
        return response;
    }

    public Customer convert(AddCustomerRequestData dto) {
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setPassword(dto.getPassword());
        customer.setRole(dto.getRole());
        return customer;
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<String>handleUnauthorized(UnAuthorizedException exception){
        String msg=exception.getMessage();
        ResponseEntity<String>response=new ResponseEntity<>(msg,HttpStatus.FORBIDDEN);
        return response;
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFound(CustomerNotFoundException ex) {
        Log.error("handleCustomerNotFound()", ex);
        String msg = ex.getMessage();
        ResponseEntity<String> response = new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        return response;
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handleAll(Throwable ex) {
        Log.error("handleAll()", ex);// this will get logged
        String msg = ex.getMessage();
        ResponseEntity<String> response = new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        return response;
    }


}
