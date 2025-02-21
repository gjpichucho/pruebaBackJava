package com.nttdata.clientservice.infrastructure.output.repository;

import com.nttdata.clientservice.infrastructure.output.repository.entity.PersonEntity;
import reactor.core.publisher.Mono;

public interface PostgrePersonRepository {

    Mono<PersonEntity> findByIdentification(String personId);

    Mono<PersonEntity> save(PersonEntity personEntity);

    Mono<Void> delete(Long id);

}
