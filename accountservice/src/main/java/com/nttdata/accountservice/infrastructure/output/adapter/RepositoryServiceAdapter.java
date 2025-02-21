package com.nttdata.accountservice.infrastructure.output.adapter;

import com.nttdata.accountservice.application.output.port.RepositoryServicePort;
import com.nttdata.accountservice.domain.Account;
import com.nttdata.accountservice.domain.Movement;
import com.nttdata.accountservice.infrastructure.exception.ModelNotFoundException;
import com.nttdata.accountservice.infrastructure.output.repository.AccountRepository;
import com.nttdata.accountservice.infrastructure.output.repository.MovementRepository;
import com.nttdata.accountservice.infrastructure.output.repository.mapper.AccountPersistenceMapper;
import com.nttdata.accountservice.infrastructure.output.repository.mapper.MovementPersistenceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class RepositoryServiceAdapter implements RepositoryServicePort {

    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;
    private final AccountPersistenceMapper accountMapper;
    private final MovementPersistenceMapper movementMapper;

    @Override
    public Flux<Account> findAccountByClientId(String clientId) {
        return accountRepository.findByClientId(clientId)
                .map(accountMapper::toAccount)
                .doOnError(error ->
                        log.error("findAccountByCustomer finished with error. ErrorDetail: {}",
                                error.getMessage()));
    }

    @Override
    public Flux<Movement> findMovementsByFilter(String clientId, OffsetDateTime startDate, OffsetDateTime endDate) {
        return movementRepository.findByFilter(clientId, startDate, endDate)
                .map(movementMapper::toMovement)
                .doOnError(error -> log.error(
                        "findMovementsByFilter finished with error. ErrorDetail: {}",
                        error.getMessage())
                );
    }

    @Override
    public Mono<Movement> findMovementsByAccountOrderByIdDesc(String accountNumber) {
        return movementRepository.findByAccountNumberOrderByIdDesc(accountNumber).next()
                .map(movementMapper::toMovement)
                .doOnError(error -> log.error(
                        "findMovementsByAccountOrderByIdDesc finished with error. ErrorDetail: {}",
                        error.getMessage())
                );
    }

    @Override
    public Mono<Account> findAccountByNumber(String accountNumber) {
        log.info("findAccountByNumber start ");
        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new ModelNotFoundException(
                        String.format("Cuenta con id: %s no encontrado", accountNumber),
                        "La cuenta no existe", HttpStatus.NOT_FOUND)))
                .map(accountMapper::toAccount)
                .doOnSuccess(response -> log.info("findAccountByNumber finished successfully"))
                .doOnError(error -> log.error("findAccountByNumber finished with error. ErrorDetail: {}", error.getMessage()));
    }

    @Override
    public Mono<Account> createAccount(Account account) {
        log.info("createAccount start ");
        return accountRepository.save(accountMapper.toAccountEntity(account))
                .map(accountMapper::toAccount)
                .doOnSuccess(response -> log.info("createAccount finished successfully"))
                .doOnError(error -> log.error("createAccount finished with error. ErrorDetail: {}", error.getMessage()));
    }

    @Override
    public Mono<Account> updateAccount(Account accountUpdate) {
        log.info("updateAccount start ");
        return accountRepository.save(accountMapper.toAccountEntity(accountUpdate))
                .map(accountMapper::toAccount)
                .doOnSuccess(response -> log.info("updateAccount finished successfully"))
                .doOnError(error -> log.error("updateAccount finished with error. ErrorDetail: {}", error.getMessage()));
    }

    @Override
    public Mono<Boolean> deleteAccount(Long accountId) {
        return accountRepository.deleteById(accountId)
                .then(Mono.just(Boolean.TRUE))
                .doOnSuccess(response -> log.info("deleteAccount finished successfully"))
                .doOnError(error ->
                        log.error("deleteAccount finished with error. ErrorDetail: {}", error.getMessage())
                );
    }

    @Override
    public Mono<Movement> findMovementById(Long movementId) {
        return movementRepository.findById(movementId)
                .map(movementMapper::toMovement)
                .doOnSuccess(response -> log.info("findMovementById finished successfully"))
                .doOnError(error ->
                        log.error("findMovementById finished with error. ErrorDetail: {}", error.getMessage())
                );
    }

    @Override
    public Mono<Movement> createMovement(Movement movement) {
        return movementRepository.save(movementMapper.toMovementEntity(movement))
                .map(movementMapper::toMovement)
                .doOnSuccess(response -> log.info("createMovement finished successfully"))
                .doOnError(error ->
                        log.error("reateMovement finished with error. ErrorDetail: {}", error.getMessage())
                );

    }

    @Override
    public Mono<Movement> updateMovement(Movement movementUpdate) {
        return movementRepository.save(movementMapper.toMovementEntity(movementUpdate))
                .map(movementMapper::toMovement)
                .doOnSuccess(response -> log.info("updateMovement finished successfully"))
                .doOnError(error ->
                        log.error("updateMovement finished with error. ErrorDetail: {}", error.getMessage())
                );
    }

    @Override
    public Mono<Boolean> deleteMovement(Long movementId) {
        return movementRepository.deleteById(movementId)
                .then(Mono.just(Boolean.TRUE))
                .doOnSuccess(response -> log.info("updateMovement finished successfully"))
                .doOnError(error ->
                        log.error("updateMovement finished with error. ErrorDetail: {}", error.getMessage())
                );
    }
}
