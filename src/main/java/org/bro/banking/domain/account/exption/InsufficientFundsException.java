package org.bro.banking.domain.account.exption;

import org.bro.banking.domain.exception.CustomExcepting;
import org.springframework.http.HttpStatus;

public class InsufficientFundsException extends CustomExcepting {
    public InsufficientFundsException() {
        super("Insufficient funds: Your account balance is too low to complete this transaction", HttpStatus.BAD_REQUEST);
    }
}
