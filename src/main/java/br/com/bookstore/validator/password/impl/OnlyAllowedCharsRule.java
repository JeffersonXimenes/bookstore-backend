package br.com.bookstore.validator.password.impl;

import br.com.bookstore.utils.Constants;
import br.com.bookstore.validator.password.PasswordRule;

public class OnlyAllowedCharsRule implements PasswordRule {

    private static final String ALLOWED_SPECIALS = "!@#$%^&*()-+";

    @Override
    public boolean isValid(String password) {
        return password.chars().allMatch(c ->
            Character.isLetterOrDigit(c) || ALLOWED_SPECIALS.indexOf(c) != -1
        );
    }

    @Override
    public String getErrorMessage() {
        return Constants.MSG_CONTAIN_INVALID_CHARACTERS;
    }
}