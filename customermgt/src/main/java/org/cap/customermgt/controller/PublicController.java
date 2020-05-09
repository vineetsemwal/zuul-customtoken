package org.cap.customermgt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/public/fruits")
@RestController
public class PublicController {

    @GetMapping
    ResponseEntity<List<String>>fruits(){
        List<String>fruits=new ArrayList<>();
        fruits.add("mango");
        fruits.add("apple");
        ResponseEntity<List<String>>response=new ResponseEntity<>(fruits, HttpStatus.OK);
        return response;
    }

}
