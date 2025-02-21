package com.nttdata.clientservice.infrastructure.output.repository;

import com.nttdata.clientservice.infrastructure.output.repository.entity.ClientEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ClientRepository extends R2dbcRepository<ClientEntity, Long> {

    Mono<ClientEntity> findByPersonId(Long personId);

}
