package br.com.bookstore.validator.password.impl;

import br.com.bookstore.utils.Constants;
import br.com.bookstore.validator.password.PasswordRule;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PasswordValidator {

    private final List<PasswordRule> rules;

    public PasswordValidator() {
        this.rules = List.of(
            new MinLengthRule(Constants.MIN_PASSWORD_VALUE),
            new UppercaseRule(),
            new LowercaseRule(),
            new DigitRule(),
            new SpecialCharRule(),
            new NoRepeatedCharRule(),
            new OnlyAllowedCharsRule()
        );
    }

    public boolean isValid(String password) {
        return rules.stream().allMatch(rule -> rule.isValid(password));
    }

    public List<String> getValidationErrors(String password) {
        return rules.stream()
            .filter(rule -> !rule.isValid(password))
            .map(PasswordRule::getErrorMessage)
            .toList();
    }
}