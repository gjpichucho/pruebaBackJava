package com.nttdata.accountservice.infrastructure.input.adapter.rest.imp;

import com.nttdata.accountservice.application.input.port.MovementServicePort;
import com.nttdata.accountservice.domain.MovementReportResponse;
import com.nttdata.accountservice.infrastructure.input.adapter.rest.mapper.MovementRestMapper;
import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.MovementRequest;
import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.MovementResponse;
import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.MovementUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@RestController
@Slf4j
@Validated
@RequestMapping("/movement")
public class MovementController {

    private final MovementServicePort movementServicePort;
    private final MovementRestMapper movementRestMapper;

    @GetMapping("/get/{id}")
    public Mono<ResponseEntity<MovementResponse>> getMovement(@PathVariable("id") Long movementId) {
        log.info("|-> [controller] getMovement start ");
        return movementServicePort.retrieveMovement(movementId)
                .map(movementRestMapper::toMovementResponse)
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("getMovement finished successfully"))
                .doOnError(error -> log.error(
                        "getMovement finished with error. ErrorDetail: {}", error.getMessage())
                );
    }

    @GetMapping("/report/{clientId}")
    public Mono<ResponseEntity<MovementReportResponse>> getMovementByFilter(@PathVariable("clientId") String clientId,
                                                                            @RequestParam("startDate") OffsetDateTime startDate,
                                                                            @RequestParam("endDate") OffsetDateTime endDate,
                                                                            ServerWebExchange exchange) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDateTime startDate = LocalDateTime.parse(startDatee, formatter);
//        LocalDateTime endDate = LocalDateTime.parse(endDatee, formatter);
        return movementServicePort.retrieveMovementsByFilter(clientId, startDate, endDate)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<MovementResponse>> postMovement(@RequestBody MovementRequest movementRequest) {
        log.info("|-> [controller] postMovement start ");
        return movementServicePort.registerMovement(movementRestMapper.toMovement(movementRequest))
                .map(movementRestMapper::toPostMovementResponse)
                .map(postMovementResponse -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(postMovementResponse))
                .doOnSuccess(response -> log.info("postMovement finished successfully"))
                .doOnError(error -> log.error("postMovement finished with error. ErrorDetail: {}", error.getMessage()));
    }


    @PutMapping("/{id}")
    public Mono<ResponseEntity<MovementResponse>> putMovement(@PathVariable("id") Long movementId,
                                                              @RequestBody MovementUpdate movementUpdate) {
        log.info("|-> [controller] putMovement start ");
        return movementServicePort.updateMovement(movementRestMapper.toMovement(movementUpdate), movementId)
                .map(movementRestMapper::toPutMovementResponse)
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("putMovement finished successfully"))
                .doOnError(error -> log.error(
                                "putMovement finished with error. ErrorDetail: {}",
                                error.getMessage()
                        )
                );
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMovement(@PathVariable("id") Long movementId) {
        log.info("|-> [controller] deleteMovement start ");
        return movementServicePort.removeMovement(movementId)
                .map(aBoolean -> ResponseEntity.ok().build());
    }
}
