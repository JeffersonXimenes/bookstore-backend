package br.com.bookstore.validator.password.impl;

import br.com.bookstore.utils.Constants;
import br.com.bookstore.validator.password.PasswordRule;

public class UppercaseRule implements PasswordRule {

    @Override
    public boolean isValid(String password) {
        return password.chars().anyMatch(Character::isUpperCase);
    }

    @Override
    public String getErrorMessage() {
        return Constants.MSG_MUST_CONTAIN_UPPER_CASE_LETTERS;
    }
}