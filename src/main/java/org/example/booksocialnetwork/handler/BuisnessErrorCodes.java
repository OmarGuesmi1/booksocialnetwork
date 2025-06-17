package org.example.booksocialnetwork.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum BuisnessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED,"No code"),
    INCORRECT_CURRENT_PASSWORD(300,BAD_REQUEST,"current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(300,BAD_REQUEST,"the new password does not match"),
    ACCOUNT_LOCKED(302,FORBIDDEN,"User Account is Locked"),
    ACCOUNT_DISABLED(303,FORBIDDEN,"User Account is Disabled"),
    BAD_CREDENTIALS(304,FORBIDDEN,"Login and / or password is incorrect"),

    ;
    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BuisnessErrorCodes(final int code, final HttpStatus httpStatus, final String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
