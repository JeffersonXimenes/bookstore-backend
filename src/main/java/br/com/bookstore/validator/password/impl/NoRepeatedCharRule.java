package br.com.bookstore.validator.password.impl;

import br.com.bookstore.utils.Constants;
import br.com.bookstore.validator.password.PasswordRule;

import java.util.HashSet;
import java.util.Set;

public class NoRepeatedCharRule implements PasswordRule {

    @Override
    public boolean isValid(String password) {
        Set<Character> seen = new HashSet<>();

        for (char c : password.toCharArray()) {
            if (!seen.add(c)) return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    @Override
    public String getErrorMessage() {
        return Constants.MSG_NO_REPEATED_CHAR;
    }
}