package com.codandotv.simplerestapi.domain.exception;

public class NoContentException extends RuntimeException {
    public NoContentException(String msg){
        super(msg);
    }
}
