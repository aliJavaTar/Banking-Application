package org.bro.banking.domain.account.exption;

import org.bro.banking.domain.exception.CustomExcepting;
import org.springframework.http.HttpStatus;

public class SameSourceAndDestinationAccountException extends CustomExcepting {
    public SameSourceAndDestinationAccountException() {
        super("Source and destination accounts are the same", HttpStatus.BAD_REQUEST);
    }
}