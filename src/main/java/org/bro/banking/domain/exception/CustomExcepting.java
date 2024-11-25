package org.bro.banking.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomExcepting extends RuntimeException {
    private final HttpStatus status;
    private ErrorType errorType;

    public CustomExcepting(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public CustomExcepting(ErrorType errorType) {

        this.status = errorType.getHttpStatus();
        this.errorType = errorType;
    }


}
