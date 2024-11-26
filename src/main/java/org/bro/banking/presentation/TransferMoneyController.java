package org.bro.banking.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bro.banking.domain.account.usecase.TransferMoney;
import org.bro.banking.presentation.dto.TransferRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/transfer")
public class TransferMoneyController {

    private final TransferMoney transferMoney;

    @PutMapping
    ResponseEntity<String> transfer(@Valid @RequestBody  TransferRequest request) {
        transferMoney.processTransfer(request);
        return ResponseEntity.ok("Transfer successful");

    }
}
