package org.bro.banking.domin.account;

public interface Accounts {
    boolean isExist(String nationalCode, String nameOfBank);

    Account addAccount(Customer customer);
}
