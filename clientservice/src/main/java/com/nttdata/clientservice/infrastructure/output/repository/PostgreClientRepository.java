package com.nttdata.clientservice.infrastructure.output.repository;

import com.nttdata.clientservice.infrastructure.output.repository.entity.ClientEntity;
import reactor.core.publisher.Mono;

public interface PostgreClientRepository {

    Mono<ClientEntity> findByPersonId(Long personId);

    Mono<ClientEntity> save(ClientEntity customerEntity);

    Mono<Void> delete(Long id);
}
