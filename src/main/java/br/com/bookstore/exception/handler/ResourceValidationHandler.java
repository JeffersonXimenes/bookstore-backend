package br.com.bookstore.exception.handler;

import br.com.bookstore.exception.ResourceValidationException;
import br.com.bookstore.exception.error.ResourceDetails;
import br.com.bookstore.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ResourceValidationHandler {

    @ExceptionHandler(ResourceValidationException.class)
    public ResponseEntity<ResourceDetails> handle(ResourceValidationException exception) {
        ResourceDetails resourceDetails = new ResourceDetails(
                new Date(),
                HttpStatus.BAD_REQUEST.value(),
                Constants.TITLE_BUSINESS_VIOLATED,
                exception.getErrors(),
                exception.getClass().getName()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resourceDetails);
    }
}