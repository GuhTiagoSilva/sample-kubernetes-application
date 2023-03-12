package com.gustavo.samplekubernetes.controllers.exceptions;

import com.gustavo.samplekubernetes.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFoundException(HttpServletRequest request,
                                                                   ResourceNotFoundException e) {
        StandardError standardError = new StandardError(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Resource Not Found",
                e.getMessage(),
                request.getRequestURI());

        return new ResponseEntity<>(standardError, HttpStatus.NOT_FOUND);
    }

}
