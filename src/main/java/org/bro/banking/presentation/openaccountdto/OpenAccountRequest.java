package org.bro.banking.presentation.openaccountdto;


import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OpenAccountRequest {

    private BigDecimal amount;
    private String nameOfBank;
    private short codeOfBank;
    private String firstname;
    private String lastname;
    private String nameOfFather;
    private String nationalCode;


}
