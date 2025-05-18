package br.com.bookstore.validator.password;

public interface PasswordRule {

    boolean isValid(String password);
    String getErrorMessage();
}