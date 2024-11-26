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


        validatePhoneNumber(request.getPhoneNumber());


        return CartResponse.builder().name(request.getFirstname())
                .family(request.getLastname())
                .expirationDate(LocalDate.now().plusYears(5))
                .cvv2(random.nextInt(random.nextInt(100, 9999)))
                .numberOfCart(generate(request.getBankId(), 16))
                .ibanNumber(REGION_CODE + generate(request.getBankId(), 24)).build();
    }

    private String generate(long bankId, int length) {

        ThreadLocalRandom random = ThreadLocalRandom.current();

        Bank bank = banks.getById(bankId).orElseThrow(() ->
                new IllegalArgumentException("this bank does not exist"));


        int randomNumberLength = length - (bank.getCode().length() + 1);

        var builder = new StringBuilder(bank.getCode());
        for (int i = 0; i < randomNumberLength; i++) {
            int digit = random.nextInt(10);
            builder.append(digit);
        }


        int checkDigit = getCheckDigit(builder.toString());
        builder.append(checkDigit);

        return builder.toString();
    }


    private int getCheckDigit(String number) {
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {

            int digit = Integer.parseInt(number.substring(i, (i + 1)));

            if ((i % 2) == 0) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = (digit / 10) + (digit % 10);
                }
            }
            sum += digit;
        }
        int mod = sum % 10;
        return ((mod == 0) ? 0 : 10 - mod);
    }

    private void validatePhoneNumber(String phoneNumber) {
        Phonenumber.PhoneNumber phone;
        try {
            phone = phoneNumberUtil.parse(phoneNumber,
                    Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
        } catch (NumberParseException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("phone number is not valid");
        }
        phoneNumberUtil.isValidNumberForRegion(phone, REGION_CODE);
    }
}
