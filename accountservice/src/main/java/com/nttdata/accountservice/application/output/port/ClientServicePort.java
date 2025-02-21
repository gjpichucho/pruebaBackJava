package com.nttdata.accountservice.application.output.port;

import com.nttdata.accountservice.domain.Client;
import reactor.core.publisher.Mono;

public interface ClientServicePort {

    Mono<Client> findClientById(String customerId);

}
