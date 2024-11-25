package org.bro.banking.presentation;

import lombok.RequiredArgsConstructor;
import org.bro.banking.domain.account.usecase.TransferMoney;
import org.bro.banking.presentation.dto.TransferRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("transfer")
public class TransferMoneyController {

    private final TransferMoney transferMoney;

    @PutMapping
    ResponseEntity<String> transfer(@RequestBody @Valid TransferRequest request) {
        transferMoney.processTransfer(request);
        return ResponseEntity.ok("Transfer successful");

    }
}
