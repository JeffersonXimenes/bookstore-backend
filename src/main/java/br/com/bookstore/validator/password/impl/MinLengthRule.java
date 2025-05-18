package br.com.bookstore.validator.password.impl;

import br.com.bookstore.utils.Constants;
import br.com.bookstore.validator.password.PasswordRule;

public class MinLengthRule implements PasswordRule {

    private final int minLength;

    public MinLengthRule(int minLength) {
        this.minLength = minLength;
    }

    @Override
    public boolean isValid(String password) {
        return password != null && password.length() >= minLength;
    }

    @Override
    public String getErrorMessage() {
        return String.format(Constants.MSG_MIN_LENGTH, minLength);
    }
}