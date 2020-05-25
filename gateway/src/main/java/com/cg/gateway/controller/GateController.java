package com.cg.gateway.controller;

import com.cg.gateway.entities.UserCredential;
import com.cg.gateway.exceptions.IncorrectCredentialsException;
import com.cg.gateway.service.CredentialService;
import com.cg.gateway.util.*;
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
    public ResponseEntity<CustomerDetails> register(@RequestBody @Valid CreateCustomerRequest requestData) {
        String url = customerServiceBaseUrl + "/add";
        CustomerDetails createCustomer = restTemplate.postForObject(url, requestData, CustomerDetails.class);
        UserCredential credential = new UserCredential();
        credential.setPassword(createCustomer.getPassword());
        credential.setRole(requestData.getRole());
        credential.setUsername(createCustomer.getUsername());
        service.save(credential);
        ResponseEntity<CustomerDetails> response = new ResponseEntity<>(createCustomer, HttpStatus.OK);
        return response;
    }

    @PostMapping("/createtoken")
    public ResponseEntity<String> createToken(@RequestBody @Valid CreateTokenRequest requestData) {
        String username = requestData.getUsername();
        String password = requestData.getPassword();
        boolean correct = service.checkCredentials(username, password);
        if (!correct) {
            throw new IncorrectCredentialsException("incorrect credentials");
        }
        UserCredential credential =service.findByUsername(username);
        String role=credential.getRole();
        String token = TokenUtil.generateToken(username, password,role);
        ResponseEntity<String> response = new ResponseEntity<>(token, HttpStatus.OK);
        return response;
    }


}
