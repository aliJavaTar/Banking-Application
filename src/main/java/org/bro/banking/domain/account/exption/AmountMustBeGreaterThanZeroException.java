package org.bro.banking.domain.account.exption;

import org.bro.banking.domain.exception.CustomExcepting;
import org.springframework.http.HttpStatus;

public class AmountMustBeGreaterThanZeroException extends CustomExcepting {
    public AmountMustBeGreaterThanZeroException() {
        super("Amount must be greater than zero", HttpStatus.BAD_REQUEST);
    }
}
