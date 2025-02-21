package com.nttdata.clientservice.application.input.port;

import com.nttdata.clientservice.domain.Client;
import com.nttdata.clientservice.domain.ClientDto;
import com.nttdata.clientservice.domain.NewClient;
import com.nttdata.clientservice.domain.UpdateClient;
import com.nttdata.clientservice.infrastructure.output.repository.entity.PersonEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IClientService {

    /**
     * Obtener todos los clientes
     *
     */
    public Flux<ClientDto> getAllClients();


    /**
     * Obtener un cliente por su id
     *
     * @pathVar idClient
     */
    public Mono<Client> getById(String id);

    Flux<PersonEntity> getAllPerson();


    /**
     * Registrar nuevo cliente
     *
     * @param newClient
     */
    public Mono<ResponseEntity<String>> registerClient(@RequestBody NewClient newClient);

    /**
     * Editar cliente
     *
     * @param updateClient
     */
    public  Mono<ResponseEntity<String>> updateClient(Long id, UpdateClient updateClient);

    /**
     * Eliminar cliente
     *
     * @param idClient
     */
    public Mono<ResponseEntity<String>> deleteClient(Long idClient);



}
