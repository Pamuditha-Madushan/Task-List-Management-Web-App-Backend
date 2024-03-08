package com.example.demo.advicers;


import com.example.demo.exception.SQLIntegrityConstraintViolationException;
import com.example.demo.exception.UnAuthorizedException;
import com.example.demo.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppWideExceptionHandler {

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<StandardResponse> handleUnAuthorizedException(UnAuthorizedException e) {
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(401, e.getMessage(), e),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<StandardResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(403, e.getMessage(), e),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<StandardResponse> handleSInternalServerErrorException(SQLIntegrityConstraintViolationException e) {
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(500, e.getMessage(), e),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(400, "Validation Error Occurred !", ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

}
