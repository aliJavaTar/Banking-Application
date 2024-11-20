package org.bro.banking.domin.bank.usecase;

import org.bro.banking.domin.bank.Banks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SigningBankShould {

    @Mock
    private Banks banks;

    @Test
    void can_not_add_new_bank_with_similar_name_and_similar_branch_and_specific_number() {
        when(banks.existByNameAndBranchCodeOrCode(anyString(), anyInt(), anyString())).thenReturn(true);

        var signing = new SigningBank(banks);

        assertThrows(IllegalArgumentException.class,
                () -> signing.register("mali", 22, "60372242"));
    }
}