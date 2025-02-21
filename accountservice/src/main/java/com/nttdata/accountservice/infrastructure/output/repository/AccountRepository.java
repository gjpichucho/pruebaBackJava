package com.nttdata.accountservice.infrastructure.output.repository;

import com.nttdata.accountservice.infrastructure.output.repository.entity.AccountEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends R2dbcRepository<AccountEntity, Long> {

    Mono<AccountEntity> findByAccountNumber(String accountNumber);

    Flux<AccountEntity> findByClientId(String clientId);
}
