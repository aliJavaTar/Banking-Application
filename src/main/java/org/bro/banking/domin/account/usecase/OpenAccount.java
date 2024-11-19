package org.bro.banking.domin.account.usecase;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bro.banking.domin.Banks;
import org.bro.banking.domin.account.Accounts;
import org.bro.banking.presentation.openaccountdto.CartResponse;
import org.bro.banking.presentation.openaccountdto.OpenAccountRequest;

import javax.validation.constraints.NotBlank;

@Slf4j
@RequiredArgsConstructor
public class OpenAccount {
    private static final String REGION_CODE = "IR";
    private final Accounts accounts;
    private final Banks banks;
    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    public CartResponse open(OpenAccountRequest request) {
        boolean exist = accounts.isExist(request.getNationalCode(), request.getBankId());
        if (exist)
            throw new IllegalArgumentException("you have an account already ");
        validatePhoneNumber(request.getPhoneNumber());
        return null;
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
