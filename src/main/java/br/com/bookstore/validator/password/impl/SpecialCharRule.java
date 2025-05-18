package br.com.bookstore.validator.password.impl;

import br.com.bookstore.utils.Constants;
import br.com.bookstore.validator.password.PasswordRule;

public class SpecialCharRule implements PasswordRule {

    private static final String SPECIALS = "!@#$%^&*()-+";

    @Override
    public boolean isValid(String password) {
        return password.chars().anyMatch(c -> SPECIALS.indexOf(c) != -1);
    }

    @Override
    public String getErrorMessage() {
        return Constants.MSG_CONTAIN_ONE_SPECIAL_CHARACTER + SPECIALS;
    }
}