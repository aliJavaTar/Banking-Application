package org.bro.banking.domain.account.exption;

import org.bro.banking.domain.exception.CustomExcepting;
import org.springframework.http.HttpStatus;

public class AccountDoesNotExist extends CustomExcepting {
    public AccountDoesNotExist(String message) {
        super(message + " account does not exist", HttpStatus.NOT_FOUND);
    }
}
