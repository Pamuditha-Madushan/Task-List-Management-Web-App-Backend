package com.example.demo.advicers;


import com.example.demo.exception.InternalServerErrorException;
import com.example.demo.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppWideExceptionHandler {

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<StandardResponse> handleSInternalServerErrorException(InternalServerErrorException e) {
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(500, e.getMessage(), e),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
