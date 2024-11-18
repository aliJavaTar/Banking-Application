package org.bro.banking.presentation.openaccountdto;


import lombok.*;

import javax.validation.constraints.Size;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OpenAccountRequest {

    private BigDecimal amount;
    private String nameOfBank;
    private int codeOfBank;
    @Size(min = 3, max = 15)
    private String firstname;
    @Size(min = 3, max = 15)
    private String lastname;
    @Size(min = 3, max = 15)
    private String nameOfFather;
    @Size(min = 10, max = 12)
    private String nationalCode;


}
