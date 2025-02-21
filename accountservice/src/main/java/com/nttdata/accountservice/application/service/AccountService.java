package com.nttdata.accountservice.application.service;

import com.nttdata.accountservice.application.input.port.AccountServicePort;
import com.nttdata.accountservice.application.output.port.ClientServicePort;
import com.nttdata.accountservice.application.output.port.RepositoryServicePort;
import com.nttdata.accountservice.domain.Account;
import com.nttdata.accountservice.domain.Movement;
import com.nttdata.accountservice.domain.TypeMovement;
import com.nttdata.accountservice.infrastructure.input.adapter.rest.mapper.AccountRestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService implements AccountServicePort {

    private final RepositoryServicePort repositoryServicePort;
    private final ClientServicePort clientservicePort;
    private final AccountRestMapper accountRestMapper;


    @Override
    public Mono<Account> retrieveAccount(String accountNumber) {
        log.info("retrieveAccount start ");
        return repositoryServicePort.findAccountByNumber(accountNumber)
                .doOnSuccess(response -> log.info("retrieveAccount finished successfully"))
                .doOnError(error -> log.error(
                                "retrieveAccount finished with error. ErrorDetail: {}",
                                error.getMessage()
                        )
                );
    }

    @Override
    public Mono<Account> registerAccount(Account account) {
        log.info("registerAccount starting");
        return clientservicePort.findClientById(account.getClientId())
                .flatMap(client -> repositoryServicePort.createAccount(account))
                .flatMap(accountSaved -> Mono.zip(Mono.just(accountSaved),
                        repositoryServicePort.createMovement(this.generateInitialMovement(accountSaved))))
                .map(tupleObjects -> tupleObjects.getT1())
                .doOnSuccess(response -> log.info("registerAccount finished successfully"))
                .doOnError(error -> log.error(
                        "registerAccount finished with error. ErrorDetail: {}",
                        error.getMessage())
                );
    }

    private Movement generateInitialMovement(Account account) {
        return Movement.builder().accountNumber(account.getAccountNumber())
                .movementDate(LocalDateTime.now())
                .movementType(TypeMovement.CREDITO.toString())
                .amount(account.getInitialBalance())
                .balance(account.getInitialBalance()).build();
    }

    @Override
    public Mono<Account> updateAccount(Account account, String accountNumber) {
        return repositoryServicePort.findAccountByNumber(accountNumber)
                .map(accountFound -> {
                            accountRestMapper.updateAccount(accountFound, account);
                            return accountFound;
                        }
                )
                .flatMap(repositoryServicePort::updateAccount)
                .doOnSuccess(response -> log.info("|-> [service] updateAccount finished successfully"))
                .doOnError(error -> log.error(
                                "|-> [service] updateAccount finished with error. ErrorDetail: {}",
                                error.getMessage()
                        )
                );
    }

    @Override
    public Mono<Boolean> removeAccount(String accountNumber) {
        return repositoryServicePort.findAccountByNumber(accountNumber)
                .flatMap(accountFound -> repositoryServicePort.deleteAccount(accountFound.getAccountId()))
                .doOnSuccess(response -> log.info("removeAccount finished successfully"))
                .doOnError(error -> log.error("removeAccount finished with error. ErrorDetail: {}", error.getMessage()));
    }
}
