package com.nttdata.clientservice.infrastructure.output.repository;

import com.nttdata.clientservice.infrastructure.output.repository.entity.PersonEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PersonRepository extends R2dbcRepository<PersonEntity, Long> {

    @Query("SELECT * FROM person WHERE identification = :ci")
    Mono<PersonEntity> findByIdentification(String ci);

}
