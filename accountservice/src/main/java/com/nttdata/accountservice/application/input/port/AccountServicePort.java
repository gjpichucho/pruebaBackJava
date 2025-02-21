package com.nttdata.accountservice.application.input.port;

import com.nttdata.accountservice.domain.Account;
import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.AccountRequest;
import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.AccountResponse;
import reactor.core.publisher.Mono;

public interface AccountServicePort {
    Mono<Account> retrieveAccount(String accountNumber);

    Mono<Account> registerAccount(Account account);

    Mono<Account> updateAccount(Account account, String accountNumber);

    Mono<Boolean> removeAccount(String accountNumber);

}
