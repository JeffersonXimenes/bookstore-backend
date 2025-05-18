package br.com.bookstore.exception.handler;

import br.com.bookstore.exception.ResourceNotFoundException;
import br.com.bookstore.exception.error.ResourceDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.List;

@ControllerAdvice
public class ResourceNotFoundExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResourceDetails> handle(ResourceNotFoundException exception) {
        ResourceDetails resourceDetails = new ResourceDetails(
                new Date(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                List.of(exception.getMessage()),
                exception.getClass().getName()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resourceDetails);
    }
}
