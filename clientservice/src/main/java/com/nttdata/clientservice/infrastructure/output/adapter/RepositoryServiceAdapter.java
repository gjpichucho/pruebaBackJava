package com.nttdata.clientservice.infrastructure.output.adapter;

import com.nttdata.clientservice.application.output.port.RepositoryServicePort;
import com.nttdata.clientservice.domain.Client;
import com.nttdata.clientservice.domain.NewClient;
import com.nttdata.clientservice.domain.UpdateClient;
import com.nttdata.clientservice.infrastructure.output.repository.PostgreClientRepository;
import com.nttdata.clientservice.infrastructure.output.repository.PostgrePersonRepository;
import com.nttdata.clientservice.infrastructure.output.repository.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class RepositoryServiceAdapter implements RepositoryServicePort {

    private final PostgreClientRepository clientRepository;
    private final PostgrePersonRepository personRepository;
    private final ClientMapper clientMapper;

    @Override
    public Mono<Client> findClientById(String id) {
        return personRepository.findByIdentification(id)
                .flatMap(personEntity -> clientRepository.findByPersonId(personEntity.getIdPerson())
                        .zipWith(Mono.just(personEntity)))
                .map(tuple -> clientMapper.toClient(tuple.getT1(), tuple.getT2()))
                .doOnSuccess((client) -> log.info("La obtención de cliente con ID: {} se completó exitosamente", client.getIdentification()))
                .doOnError(error -> log.error("Ocurrió un error al obtener el cliente: {}", error.getMessage()));
    }

    @Override
    public Mono<Client> registerClient(NewClient newClient) {
        return null;
    }

    @Override
    public Mono<Client> updateClient(Long id, UpdateClient updateClient) {
        return null;
    }

    @Override
    public Mono<Boolean> deleteClient(Long idClient) {
        return null;
    }
}
