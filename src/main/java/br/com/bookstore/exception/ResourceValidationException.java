package br.com.bookstore.exception;

import br.com.bookstore.utils.Constants;
import lombok.Getter;

import java.util.List;

@Getter
public class ResourceValidationException extends RuntimeException {

    private final List<String> errors;

    public ResourceValidationException(List<String> errors) {
        super(Constants.TITLE_BUSINESS_VIOLATED);
        this.errors = errors;
    }
}