package org.cap.customermgt.exceptions;

public class UnAuthorizedException extends RuntimeException{
    public UnAuthorizedException(String msg){
        super(msg);
    }
}
