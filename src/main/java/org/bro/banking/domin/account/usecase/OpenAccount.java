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


        ThreadLocalRandom random1 = ThreadLocalRandom.current();

        Bank bank = banks.getById(request.getBankId()).orElseThrow(() ->
                new IllegalArgumentException("this bank does not exist"));


        int randomNumberLength = 24 - (bank.getCode().length() + 1);

        var builder = new StringBuilder(bank.getCode());
        for (int i = 0; i < randomNumberLength; i++) {
            int digit = random1.nextInt(10);
            builder.append(digit);
        }


        String number1 = builder.toString();
        int sum1 = 0;
        for (int i1 = 0; i1 < number1.length(); i1++) {

            int digit1 = Integer.parseInt(number1.substring(i1, (i1 + 1)));

            if ((i1 % 2) == 0) {
                digit1 = digit1 * 2;
                if (digit1 > 9) {
                    digit1 = (digit1 / 10) + (digit1 % 10);
                }
            }
            sum1 += digit1;
        }
        int mod1 = sum1 % 10;
        int checkDigit = ((mod1 == 0) ? 0 : 10 - mod1);
        builder.append(checkDigit);

        ThreadLocalRandom random2 = ThreadLocalRandom.current();

        Bank bank1 = banks.getById(request.getBankId()).orElseThrow(() ->
                new IllegalArgumentException("this bank does not exist"));


        int randomNumberLength1 = 16 - (bank1.getCode().length() + 1);

        var builder1 = new StringBuilder(bank1.getCode());
        for (int i = 0; i < randomNumberLength1; i++) {
            int digit = random2.nextInt(10);
            builder1.append(digit);
        }


        String number = builder1.toString();
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
        int checkDigit1 = ((mod == 0) ? 0 : 10 - mod);
        builder1.append(checkDigit1);

        return CartResponse.builder().name(request.getFirstname())
                .family(request.getLastname())
                .expirationDate(LocalDate.now().plusYears(5))
                .cvv2(random.nextInt(random.nextInt(100, 9999)))
                .mainPassword(random.nextInt(random.nextInt(1000, 9999)))
                .secondPassword(random.nextLong(random.nextInt(100000000, 999999999)))
                .numberOfCart(builder1.toString())
                .ibanNumber(REGION_CODE + builder.toString()).build();
    }


}
