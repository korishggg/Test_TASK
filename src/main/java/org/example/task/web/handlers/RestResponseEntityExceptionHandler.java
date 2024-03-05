package org.example.task.web.handlers;

import org.example.task.dto.ErrorStatus;
import org.example.task.exception.ClientErrorException;
import org.example.task.exception.ResourceNotFoundException;
import org.example.task.exception.UnsupportedContentTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorStatus(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
    }

    @ExceptionHandler(value = ClientErrorException.class)
    public ResponseEntity<Object> handleClientErrorException(ClientErrorException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorStatus(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
    }
}


