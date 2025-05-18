package br.com.bookstore.utils;

public interface Constants {

//  password validation
    static final Integer MIN_PASSWORD_VALUE                 = 9;
    static final String MSG_MUST_CONTAIN_LOWER_CASE_LETTERS = "A senha deve conter ao menos uma letra minuscula.";
    static final String MSG_MUST_CONTAIN_UPPER_CASE_LETTERS = "A senha deve conter ao menos uma letra maiúscula.";
    static final String MSG_MIN_LENGTH                      = "A senha deve ter pelo menos %s caracteres.";
    static final String MSG_NO_REPEATED_CHAR                = "A senha não deve conter caracteres repetidos.";
    static final String MSG_CONTAIN_ONE_DIGIT               = "A senha deve conter ao menos um dígito.";
    static final String MSG_CONTAIN_ONE_SPECIAL_CHARACTER   = "A senha deve conter ao menos um caractere especial: ";
    static final String MSG_CONTAIN_INVALID_CHARACTERS      = "A senha contém caracteres inválidos.";

//  validation messages regarding exception
    static final String USER_NOT_FOUND          = "User Not Found" ;
    static final String ROLE_NOT_FOUND          = "Role [ %s ] not found" ;
    static final String USER_ALREADY_REGISTERED = "Usuário já cadastrado!" ;

//  message title exception
    static final String TITLE_BUSINESS_VIOLATED = "Regra de negócio violada";
}
