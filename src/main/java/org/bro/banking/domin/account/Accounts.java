package org.bro.banking.domin.account;

import org.bro.banking.domin.Customer;

public interface Accounts {
    boolean isExist(String nationalCode, String nameOfBank);

    Account addAccount(Customer customer);
}
