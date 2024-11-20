package org.bro.banking.domin.account.usecase;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bro.banking.domin.Bank;
import org.bro.banking.domin.Banks;
import org.bro.banking.domin.account.Accounts;
import org.bro.banking.presentation.openaccountdto.CartResponse;
import org.bro.banking.presentation.openaccountdto.OpenAccountRequest;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
public class OpenAccount {

    private static final String REGION_CODE = "IR";
    private final Accounts accounts;
    private final Banks banks;
    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    ThreadLocalRandom random = ThreadLocalRandom.current();

    public CartResponse open(OpenAccountRequest request) {
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


        return CartResponse.builder().name(request.getFirstname())
                .family(request.getLastname())
                .expirationDate(LocalDate.now().plusYears(5))
                .cvv2(random.nextInt(random.nextInt(100, 9999)))
                .mainPassword(random.nextInt(random.nextInt(1000, 9999)))
                .secondPassword(random.nextLong(random.nextInt(100000000, 999999999)))
                .numberOfCart(generate(request.getBankId(), 12))
                .ibanNumber(REGION_CODE + generate(request.getBankId(), 22)).build();
    }

    private String generate(long bankId, int length) {
        boolean tryAgain = true;

        ThreadLocalRandom random = ThreadLocalRandom.current();

        Bank bank = banks.getById(bankId).orElseThrow(() ->
                new IllegalArgumentException("this bank does not exist"));
        var builder = new StringBuilder();

        while (tryAgain) {

            builder.append(bank.getCode());
            for (int i = 0; i < length; i++) {
                int digit = random.nextInt(10);
                builder.append(digit);
            }
            var cardNumber = builder.toString();
            if (checkLuhn(cardNumber))
                tryAgain = false;
            else
                builder.delete(0, cardNumber.length());

        }
        return builder.toString();
    }


    static boolean checkLuhn(String cardNo) {
        int nDigits = cardNo.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {

            int d = cardNo.charAt(i) - '0';

            if (isSecond)
                d = d * 2;

            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }

}
