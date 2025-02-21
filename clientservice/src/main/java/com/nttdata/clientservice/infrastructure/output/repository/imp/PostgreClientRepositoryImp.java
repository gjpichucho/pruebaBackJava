package com.nttdata.clientservice.infrastructure.output.repository.imp;

import com.nttdata.clientservice.infrastructure.exception.DataAccessCustomException;
import com.nttdata.clientservice.infrastructure.exception.ModelNotFoundException;
import com.nttdata.clientservice.infrastructure.output.repository.ClientRepository;
import com.nttdata.clientservice.infrastructure.output.repository.PostgreClientRepository;
import com.nttdata.clientservice.infrastructure.output.repository.entity.ClientEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
@RequiredArgsConstructor
public class PostgreClientRepositoryImp implements PostgreClientRepository {

    private final ClientRepository clientRepository;

    @Override
    public Mono<ClientEntity> findByPersonId(Long personId) {
        return clientRepository.findByPersonId(personId)
                .switchIfEmpty(Mono.error(new ModelNotFoundException(String.format("Persona con identifiacion: %d no encontrado", personId),
                        "El cliente no existe", HttpStatus.NOT_FOUND)));
    }

    @Override
    public Mono<ClientEntity> save(ClientEntity customerEntity) {
        return clientRepository.save(customerEntity)
                .doOnSuccess(response -> log.info(
                        "|-> [repository] saveCustomer finished successfully."))
                .doOnError(error -> log.error(
                                "|-> [repository] saveCustomer finished with error. ErrorDetail: {}",
                                error.getMessage()
                        )
                )
                .onErrorMap(error -> new DataAccessCustomException("Error en el registro del cliente. DataAccess", error.getMessage(),
                        HttpStatus.BAD_REQUEST));
    }

    @Override
    public Mono<Void> delete(Long id) {
        return clientRepository.deleteById(id);
    }
}
