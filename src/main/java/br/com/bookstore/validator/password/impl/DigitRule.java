package br.com.bookstore.validator.password.impl;

import br.com.bookstore.utils.Constants;
import br.com.bookstore.validator.password.PasswordRule;

public class DigitRule implements PasswordRule {

    @Override
    public boolean isValid(String password) {
        return password.chars().anyMatch(Character::isDigit);
    }

    @Override
    public String getErrorMessage() {
        return Constants.MSG_CONTAIN_ONE_DIGIT;
    }
}