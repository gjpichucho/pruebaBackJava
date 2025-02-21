package com.nttdata.accountservice.application.service;

import com.nttdata.accountservice.application.input.port.MovementServicePort;
import com.nttdata.accountservice.application.output.port.ClientServicePort;
import com.nttdata.accountservice.application.output.port.RepositoryServicePort;
import com.nttdata.accountservice.domain.Account;
import com.nttdata.accountservice.domain.Movement;
import com.nttdata.accountservice.domain.MovementReportResponse;
import com.nttdata.accountservice.domain.TypeMovement;
import com.nttdata.accountservice.infrastructure.exception.BussinessValidException;
import com.nttdata.accountservice.infrastructure.exception.ModelNotFoundException;
import com.nttdata.accountservice.infrastructure.input.adapter.rest.mapper.MovementRestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovementService implements MovementServicePort {

    private final RepositoryServicePort repositoryServicePort;
    private final MovementRestMapper movementRestMapper;
    private final ClientServicePort clientservicePort;

    @Override
    public Mono<Movement> retrieveMovement(Long movementId) {
        return repositoryServicePort.findMovementById(movementId)
                .doOnSuccess(response -> log.info("retrieveMovement finished successfully"))
                .doOnError(error -> log.error(
                        "retrieveMovement finished with error. ErrorDetail: {}",
                        error.getMessage())
                );
    }

    @Override
    public Mono<MovementReportResponse> retrieveMovementsByFilter(String clientId, OffsetDateTime startDate, OffsetDateTime endDate) {
        return repositoryServicePort.findAccountByClientId(
                        clientId
                )
                .flatMap(account -> Mono.zip(
                                        Mono.just(account),
                                        repositoryServicePort.findMovementsByFilter(
                                                        clientId,
                                                        startDate,
                                                        endDate
                                                )
                                                .filter(movement -> movement.getAccountNumber()
                                                        .equals(account.getAccountNumber())
                                                )
                                                .collectList()
                                )
                                .map(tupleObjects -> movementRestMapper.toMovementReport(
                                                tupleObjects.getT1(),
                                                tupleObjects.getT2()
                                        )
                                )
                )
                .collectList()
                .flatMap(movementReports -> Mono.zip(
                                clientservicePort.findClientById(clientId),
                                Mono.just(movementReports)
                        )
                )
                .map(tupleObjects -> movementRestMapper.toMovementReportResponse(
                                tupleObjects.getT1(),
                                tupleObjects.getT2()
                        )
                );
    }

    @Override
    public Mono<Movement> registerMovement(Movement movement) {
        return getAccount(movement.getAccountNumber())
                .flatMap(account -> {
                    if (movement.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                        return Mono.error(new BussinessValidException("El valor del movimiento no puede ser cero"));
                    }
                    return repositoryServicePort.findMovementsByAccountOrderByIdDesc(movement.getAccountNumber())
                            .flatMap(lastMovement -> createMovement(movement, account, lastMovement));
                })
                .flatMap(movement1 -> repositoryServicePort.createMovement(movement1))
                .doOnSuccess(response -> log.info(" registerMovement finished successfully"))
                .doOnError(error ->
                        log.error("registerMovement finished with error. ErrorDetail: {}",
                                error.getMessage()));

    }

    private Mono<Account> getAccount(String accountNumber) {
        return repositoryServicePort.findAccountByNumber(accountNumber)
                .switchIfEmpty(Mono.error(new ModelNotFoundException(
                        "Número de cuenta: " + accountNumber + " no encontrada",
                        "cuenta no encontrada, proporcione una cuenta correcta", HttpStatus.BAD_REQUEST)));
    }

    private Mono<Movement> createMovement(Movement newMovement, Account account, Movement movements) {
        Movement movement = new Movement();
        movement.setAccountNumber(account.getAccountNumber());

        return determineMovementTypeAndBalance(newMovement, movements)
                .doOnNext(m -> {
                    movement.setMovementType(m.getMovementType());
                    movement.setAmount(m.getAmount());
                    movement.setBalance(m.getBalance());
                })
                .thenReturn(movement);
    }

    private Mono<Movement> determineMovementTypeAndBalance(Movement newMovement, Movement latestMovement) {
        BigDecimal newValue = newMovement.getAmount();
        return Mono.just(latestMovement)
                .filter(m -> newValue.signum() == 1)
                .map(m -> {
                    Movement creditMovement = new Movement();
                    creditMovement.setMovementType(TypeMovement.CREDITO.toString());
                    creditMovement.setAmount(newValue);
                    creditMovement.setBalance(newValue.add(m.getBalance()));
                    return creditMovement;
                })
                .switchIfEmpty(Mono.just(latestMovement)
                        .filter(m -> m.getBalance().subtract(newValue.abs()).signum() != -1)
                        .map(m -> {
                            Movement debitMovement = new Movement();
                            debitMovement.setMovementType(TypeMovement.DEBITO.toString());
                            debitMovement.setAmount(newValue);
                            debitMovement.setBalance(m.getBalance().subtract(newValue.abs()));
                            return debitMovement;
                        })
                        .switchIfEmpty(Mono.error(new BussinessValidException(
                                "Saldo no Disponible. El valor del movimiento no puede ser superior al del saldo actual")))
                );
    }


    @Override
    public Mono<Movement> updateMovement(Movement movement, Long movementId) {
        return repositoryServicePort.findMovementById(movementId)
                .map(movementFound -> {
                            movementRestMapper.updateMovement(movementFound, movement);
                            return movementFound;
                        }
                )
                .flatMap(repositoryServicePort::updateMovement)
                .doOnSuccess(response -> log.info(" updateMovement finished successfully"))
                .doOnError(error ->
                        log.error("updateMovement finished with error. ErrorDetail: {}",
                                error.getMessage()));
    }

    @Override
    public Mono<Boolean> removeMovement(Long movementId) {
        return repositoryServicePort.findMovementById(movementId)
                .flatMap(movementFound -> repositoryServicePort.deleteMovement(movementFound.getMovementId()))
                .doOnSuccess(response -> log.info("removeMovement finished successfully"))
                .doOnError(error -> log.error(
                        "removeMovement finished with error. ErrorDetail: {}", error.getMessage())
                );
    }
}
