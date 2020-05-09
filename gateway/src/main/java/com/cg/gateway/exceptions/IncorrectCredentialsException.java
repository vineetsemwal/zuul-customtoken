package com.cg.gateway.exceptions;

public class IncorrectCredentialsException extends RuntimeException {
    public IncorrectCredentialsException(String msg) {
        super(msg);
    }
}
