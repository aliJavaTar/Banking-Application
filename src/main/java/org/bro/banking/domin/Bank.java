package org.bro.banking.domin;

import lombok.Getter;
import org.bro.banking.domin.account.Account;

import java.util.Set;

@Getter
public class Bank {
    private String name;
    private Set<Account> accounts;
}
