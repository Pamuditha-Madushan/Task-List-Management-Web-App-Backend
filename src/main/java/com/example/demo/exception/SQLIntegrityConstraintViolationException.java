package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class SQLIntegrityConstraintViolationException extends RuntimeException {

    public SQLIntegrityConstraintViolationException(String message) {
        super(message);
    }
}
