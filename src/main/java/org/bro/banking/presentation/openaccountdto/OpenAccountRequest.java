package org.bro.banking.presentation.openaccountdto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OpenAccountRequest {
    private long bankId;
    private BigDecimal amount;
    @Size(min = 3, max = 15)
    private String firstname;
    @Size(min = 3, max = 15)
    private String lastname;
    @Size(min = 3, max = 15)
    private String nameOfFather;
    @Size(min = 10, max = 12)
    private String nationalCode;
    @NotBlank
    private String address;
    @NotBlank
    private String phoneNumber;
}
