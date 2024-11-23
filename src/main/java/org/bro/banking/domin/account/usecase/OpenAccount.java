package org.bro.banking.domin.account.usecase;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bro.banking.domin.bank.Bank;
import org.bro.banking.domin.bank.Banks;
import org.bro.banking.domin.account.Accounts;
import org.bro.banking.presentation.openaccountdto.CartResponse;
import org.bro.banking.presentation.openaccountdto.OpenAccountRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class OpenAccount {

    private static final String REGION_CODE = "IR";
    private final Accounts accounts;
    private final Banks banks;
    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    ThreadLocalRandom random = ThreadLocalRandom.current();

    public CartResponse add(OpenAccountRequest request) {
        boolean exist = accounts.isExist(request.getNationalCode(), request.getBankId());
        if (exist)
            throw new IllegalArgumentException("you have an account already ");


        Phonenumber.PhoneNumber phone;
        try {
            phone = phoneNumberUtil.parse(request.getPhoneNumber(),
                    Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
        } catch (NumberParseException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("phone number is not valid");
        }
        phoneNumberUtil.isValidNumberForRegion(phone, REGION_CODE);

        Bank bank = banks.getById(request.getBankId()).orElseThrow(() ->
                new IllegalArgumentException("this bank does not exist"));

        return CartResponse.builder().name(request.getFirstname())
                .family(request.getLastname())
                .expirationDate(LocalDate.now().plusYears(5))
                .cvv2(random.nextInt(random.nextInt(100, 9999)))
                .mainPassword(random.nextInt(random.nextInt(1000, 9999)))
                .secondPassword(random.nextLong(random.nextInt(100000000, 999999999)))
                .numberOfCart(generateCardNumber(bank.getCode()))
                .ibanNumber(generate()).build();
    }

    private String generate() {

        var builder = new StringBuilder();
        builder.append(REGION_CODE);
        for (int i = 0; i < 22; i++) {
            int digit = random.nextInt(10);
            builder.append(digit);
        }
        return builder.toString();

    }

    private String generateCardNumber(String codeBank) {

        Set<String> curd = new HashSet<>();
        var builder = new StringBuilder();
        for (int index = 0; index < 10000000; index++) {
            boolean tryAgain = true;
            while (tryAgain) {
                builder.append(codeBank);
                for (int i = 0; i < 12; i++) {
                    int digit = random.nextInt(10);
                    builder.append(digit);
                }
                var cardNumber = builder.toString();
                if (validationCardNumber(cardNumber))
                    tryAgain = false;
                else
                    builder.delete(0, cardNumber.length());

            }

            curd.add(builder.toString());
        }

        System.out.println(curd.size());
        System.out.println("--------------------------------");
        return builder.toString();
    }


    boolean validationCardNumber(String cardNo) {
        int nDigits = cardNo.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int index = nDigits - 1; index >= 0; index--) {

            int d = cardNo.charAt(index) - '0';

            if (isSecond)
                d = d * 2;

            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }

}
