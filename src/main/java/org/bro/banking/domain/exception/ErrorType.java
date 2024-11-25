package org.bro.banking.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    DESTINATION_OR_SOURCE_ACCOUNT_EXPIRED(1, HttpStatus.BAD_REQUEST);

    private final int errorCode;
    private final HttpStatus httpStatus;

    ErrorType(int errorCode, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

}
