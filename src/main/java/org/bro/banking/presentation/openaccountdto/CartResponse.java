package org.bro.banking.presentation.openaccountdto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartResponse {

    private String name;
    private String family;
    private short cvv2;
    private LocalDate expirationDate;
    private String numberOfCart;
    private String ibanNumber;
}
