package br.com.bookstore.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProfileEnum {

    USER("USER");

    private final String description;
}
