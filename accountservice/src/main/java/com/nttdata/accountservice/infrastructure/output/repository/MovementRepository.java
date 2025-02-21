package com.nttdata.accountservice.infrastructure.output.repository;

import com.nttdata.accountservice.infrastructure.output.repository.entity.MovementEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public interface MovementRepository extends R2dbcRepository<MovementEntity, Long> {

    @Query("select m.* from movement m inner join account a on a.id_person = :clientId where m.movement_date between :startDate and :endDate group by m.id_movement")
    Flux<MovementEntity> findByFilter(String clientId, OffsetDateTime startDate, OffsetDateTime endDate);

    Flux<MovementEntity> findByAccountNumberOrderByIdDesc(String accountNumber);
}
