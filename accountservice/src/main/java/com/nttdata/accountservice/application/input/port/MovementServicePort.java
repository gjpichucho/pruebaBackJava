package com.nttdata.accountservice.application.input.port;

import com.nttdata.accountservice.domain.Movement;
import com.nttdata.accountservice.domain.MovementReportResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public interface MovementServicePort {

    Mono<Movement> retrieveMovement(Long movementId);

    Mono<MovementReportResponse> retrieveMovementsByFilter(String clientId,
                                                           OffsetDateTime startDate,
                                                           OffsetDateTime endDate);

    Mono<Movement> registerMovement(Movement movement);

    Mono<Movement> updateMovement(Movement movement, Long movementId);

    Mono<Boolean> removeMovement(Long movementId);

}
