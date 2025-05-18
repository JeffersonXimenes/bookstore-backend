package br.com.bookstore.validator.password.impl;

import br.com.bookstore.utils.Constants;
import br.com.bookstore.validator.password.PasswordRule;

public class LowercaseRule implements PasswordRule {

    @Override
    public boolean isValid(String password) {
        return password.chars().anyMatch(Character::isLowerCase);
    }

    @Override
    public String getErrorMessage() {
        return Constants.MSG_MUST_CONTAIN_LOWER_CASE_LETTERS;
    }
}