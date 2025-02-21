package com.nttdata.clientservice.application.output.port;

import com.nttdata.clientservice.domain.Client;
import com.nttdata.clientservice.domain.ClientDto;
import com.nttdata.clientservice.domain.NewClient;
import com.nttdata.clientservice.domain.UpdateClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RepositoryServicePort {

    Mono<Client> findClientById(String id);

    Mono<Client> registerClient(NewClient newClient);

    Mono<Client> updateClient(Long id, UpdateClient updateClient);

    Mono<Boolean> deleteClient(Long idClient);

}
