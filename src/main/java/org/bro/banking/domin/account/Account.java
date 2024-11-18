package org.bro.banking.domin.account;

import lombok.Getter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Getter
public class Account {
    private Long accountId;
    private LocalDate expireDate;
    private String ibanNumber;
    private String cardNumber;
    private Integer cvv2;


    public Account generatedCard(String nameOfBank) {
        var random = new Random();
        this.expireDate = LocalDate.now().plusYears(5);
        this.cvv2 = 100 + random.nextInt(900);
        this.cardNumber = generateCardNumber(nameOfBank, random);
        return this;
    }

    private String generateCardNumber(String nameOfBank, Random random) {
        Map<String, Integer> forthDigits = initializeBankNames();
        boolean isExist = forthDigits.containsKey(nameOfBank);
        if (!isExist)
            throw new IllegalArgumentException("this name of bank not exists");
        long number = random.nextLong(1000000000000L, 9999999999999L);
        Integer unique = forthDigits.get(nameOfBank);

        return String.valueOf(unique).concat(String.valueOf(number));
    }


    private Map<String, Integer> initializeBankNames() {
        final int saderatCode = 1232;
        Map<String, Integer> forthDigits = new HashMap<>();
        forthDigits.put("saderat", saderatCode);
        return forthDigits;
    }
}
