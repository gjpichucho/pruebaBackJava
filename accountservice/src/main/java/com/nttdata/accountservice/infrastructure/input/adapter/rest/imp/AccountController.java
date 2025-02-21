package com.nttdata.accountservice.infrastructure.input.adapter.rest.imp;

import com.nttdata.accountservice.application.input.port.AccountServicePort;
import com.nttdata.accountservice.infrastructure.input.adapter.rest.mapper.AccountRestMapper;
import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.AccountRequest;
import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.AccountResponse;
import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.AccountUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@RestController
@Slf4j
@Validated
@RequestMapping("/account")
public class AccountController {

    private final AccountServicePort accountServicePort;
    private final AccountRestMapper accountRestMapper;

    @GetMapping("/get/{id}")
    public Mono<ResponseEntity<AccountResponse>> getAccountByAccountNumber(@PathVariable("id") String accountNumber) {
        return accountServicePort.retrieveAccount(accountNumber)
                .map(account -> accountRestMapper.toAccountresponse(account))
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("[controller] getAccount finished successfully"))
                .doOnError(error -> log.error(
                                "** [controller] getAccount finished with error **. ErrorDetail: {}",
                                error.getMessage()
                        )
                );
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<AccountResponse>> postAccount(@Valid @RequestBody AccountRequest accountRequest) {
        return accountServicePort.registerAccount(accountRestMapper.toAccount(accountRequest))
                .map(account -> accountRestMapper.toPostAccountResponse(account))
                .map(postAccountResponse -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(postAccountResponse))
                .doOnSuccess(response -> log.info("postAccount finished successfully"))
                .doOnError(error -> log.error(
                                "postAccount finished with error. ErrorDetail: {}",
                                error.getMessage()
                        )
                );
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<AccountResponse>> putAccount(@PathVariable("id") String accountNumber,
                                                            @RequestBody AccountUpdate accountUpdate) {

        return accountServicePort.updateAccount(accountRestMapper.toAccount(accountUpdate), accountNumber)
                .map(account -> accountRestMapper.toPutAccountResponse(account))
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("putAccount finished successfully"))
                .doOnError(error -> log.error("putAccount finished with error. ErrorDetail: {}", error.getMessage()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAccount(@PathVariable("id") String accountNumber) {
        log.info("deleteAccount start ");
        return accountServicePort.removeAccount(accountNumber).map(c -> ResponseEntity.ok().build());
    }


}
