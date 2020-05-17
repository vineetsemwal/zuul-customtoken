package com.cg.gateway.controller;

import com.cg.gateway.entities.UserCredential;
import com.cg.gateway.exceptions.IncorrectCredentialsException;
import com.cg.gateway.service.CredentialService;
import com.cg.gateway.util.*;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@RestController
@Validated
public class GateController {
    private static final Logger Log = LoggerFactory.getLogger(GateController.class);

    @Value("${customerservice.baseurl}")
    private String customerServiceBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CredentialService service;

    @PostMapping("/register")
    public ResponseEntity<CustomerDto> register(@RequestBody @Valid  CreateCustomerRequestData requestData) {
        String url = customerServiceBaseUrl + "/add";
        CustomerDto createCustomer = restTemplate.postForObject(url, requestData, CustomerDto.class);
        UserCredential credential = new UserCredential();
        String id = String.valueOf(createCustomer.getId());
        credential.setId(id);
        credential.setPassword(createCustomer.getPassword());
        credential.setRole(requestData.getRole());
        Log.info("inside register,id=" + createCustomer.getId() +
                " password=" + createCustomer.getPassword() +" role="+credential.getRole());
        service.save(credential);
        ResponseEntity<CustomerDto> response = new ResponseEntity<>(createCustomer, HttpStatus.OK);
        return response;
    }

    @PostMapping("/createtoken")
    public ResponseEntity<String> createToken(@RequestBody CredentialDto credentialData) {
        String id = credentialData.getId();
        String password = credentialData.getPassword();
        boolean correct = service.checkCredentials(id, password);
        if (!correct) {
            throw new IncorrectCredentialsException("incorrect credentials");
        }
        UserCredential credential =service.findById(id);
        String role=credential.getRole();
        String token = TokenUtil.generateToken(id, password,role);
        ResponseEntity<String> response = new ResponseEntity<>(token, HttpStatus.OK);
        return response;
    }


    @ExceptionHandler(IncorrectCredentialsException.class)
    public ResponseEntity<String> handleIncorrectCredentials(IncorrectCredentialsException exception) {
        String msg = exception.getMessage();
        ResponseEntity<String> response = new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        return response;
    }


}
