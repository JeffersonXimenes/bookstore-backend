package br.com.bookstore.exception.handler;

import br.com.bookstore.exception.ResourceExpiredJwtException;
import br.com.bookstore.exception.error.ResourceDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ResourceExpiredJwtExceptionHandler {

    private static final String TITLE_EXPIRED_OR_INVALID = "Token expirado ou inválido";

    @ExceptionHandler(ResourceExpiredJwtException.class)
    public ResponseEntity<ResourceDetails> handle(ResourceExpiredJwtException exception) {
        ResourceDetails resourceDetails = new ResourceDetails(
                new Date(),
                HttpStatus.UNAUTHORIZED.value(),
                TITLE_EXPIRED_OR_INVALID,
                null,
                exception.getClass().getName()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resourceDetails);
    }
}
