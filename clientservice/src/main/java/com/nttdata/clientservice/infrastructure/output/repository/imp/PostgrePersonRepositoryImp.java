package com.nttdata.clientservice.infrastructure.output.repository.imp;

import com.nttdata.clientservice.infrastructure.exception.DataAccessCustomException;
import com.nttdata.clientservice.infrastructure.exception.DatabaseException;
import com.nttdata.clientservice.infrastructure.exception.ModelNotFoundException;
import com.nttdata.clientservice.infrastructure.output.repository.PersonRepository;
import com.nttdata.clientservice.infrastructure.output.repository.PostgrePersonRepository;
import com.nttdata.clientservice.infrastructure.output.repository.entity.PersonEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
@RequiredArgsConstructor
public class PostgrePersonRepositoryImp implements PostgrePersonRepository {

    private final PersonRepository personRepository;

    @Override
    public Mono<PersonEntity> findByIdentification(String personId) {
        log.info("[output-adapter] findByIdentification start  ");
        return personRepository.findByIdentification(personId)
                .switchIfEmpty(Mono.error(new ModelNotFoundException(String.format("Persona con identifiacion: %s no encontrado", personId),
                        "El cliente no existe", HttpStatus.NOT_FOUND)))
                .onErrorMap(DatabaseException::new)
                .doOnSuccess(response -> log.info(
                        "[output-adapter] findByIdentification finished successfully"))
                .doOnError(error -> log.error(
                                "[output-adapter] findByIdentification finished with error. ErrorDetail: {} ",
                                error.getMessage()
                        )
                );
    }

    @Transactional
    @Override
    public Mono<PersonEntity> save(PersonEntity personEntity) {
        log.info("savePerson start");
        return personRepository.save(personEntity)
                .doOnSuccess(response -> log.info("savePerson finished successfully."))
                .doOnError(error -> log.error(
                                "savePerson finished with error. ErrorDetail: {}",
                                error.getMessage()
                        )
                )
                .onErrorMap(error -> new DataAccessCustomException("Error en el registro del cliente. DataAccess", error.getMessage(),
                        HttpStatus.BAD_REQUEST));
    }

    @Override
    public Mono<Void> delete(Long id) {
        return personRepository.deleteById(id).doOnSuccess(response -> log.info("delete finished successfully"))
                .doOnError(error -> log.error(
                                "|-> [repository] delete finished with error. ErrorDetail: {}",
                                error.getMessage()
                        )
                )
                .onErrorMap(throwable -> new DataAccessCustomException("Error al eliminar el cliente. DataAccess", throwable.getMessage(),
                        HttpStatus.BAD_REQUEST)
                );
    }
}
