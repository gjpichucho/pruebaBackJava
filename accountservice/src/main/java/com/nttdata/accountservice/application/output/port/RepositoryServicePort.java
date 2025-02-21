package com.nttdata.accountservice.application.output.port;

import com.nttdata.accountservice.domain.Account;
import com.nttdata.accountservice.domain.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public interface RepositoryServicePort {
    Flux<Account> findAccountByClientId(String clientId);

    Flux<Movement> findMovementsByFilter(String clientId,
                                         OffsetDateTime startDate,
                                         OffsetDateTime endDate);


    Mono<Movement> findMovementsByAccountOrderByIdDesc(String accountNumber);

    Mono<Account> findAccountByNumber(String accountNumber);

    Mono<Account> createAccount(Account account);

    Mono<Account> updateAccount(Account accountUpdate);

    Mono<Boolean> deleteAccount(Long accountId);

    Mono<Movement> findMovementById(Long movementId);

    Mono<Movement> createMovement(Movement movement);

    Mono<Movement> updateMovement(Movement movementUpdate);

    Mono<Boolean> deleteMovement(Long movementId);

}
