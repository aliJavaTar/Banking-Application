package org.bro.banking.presentation;

import lombok.RequiredArgsConstructor;
import org.bro.banking.domin.account.usecase.OpenAccount;
import org.bro.banking.presentation.openaccountdto.CartResponse;
import org.bro.banking.presentation.openaccountdto.OpenAccountRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("accounts")
public class OpenAccountController {

    private final OpenAccount openAccount;

    @PostMapping()
    public ResponseEntity<CartResponse> addNewAccount(@Valid @RequestBody OpenAccountRequest request) {
        CartResponse response = openAccount.add(request);
        return ResponseEntity.ok(response);
    }
}
