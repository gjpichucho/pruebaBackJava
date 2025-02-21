package com.nttdata.clientservice.application.service;

import com.nttdata.clientservice.application.config.AES256;
import com.nttdata.clientservice.application.config.LoggerService;
import com.nttdata.clientservice.application.output.port.RepositoryServicePort;
import com.nttdata.clientservice.domain.Client;
import com.nttdata.clientservice.infrastructure.exception.BusinessLogicException;
import com.nttdata.clientservice.domain.ClientDto;
import com.nttdata.clientservice.domain.NewClient;
import com.nttdata.clientservice.domain.UpdateClient;
import com.nttdata.clientservice.infrastructure.output.repository.entity.ClientEntity;
import com.nttdata.clientservice.infrastructure.output.repository.entity.PersonEntity;
import com.nttdata.clientservice.infrastructure.output.repository.ClientRepository;
import com.nttdata.clientservice.infrastructure.output.repository.PersonRepository;
import com.nttdata.clientservice.application.input.port.IClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImp implements IClientService {

    private final ClientRepository clientRepository;
    private final RepositoryServicePort repositoryServicePort;
    private final PersonRepository personRepository;
    private final LoggerService logger;
    private final AES256 aes256;

    @Override
    public Flux<ClientDto> getAllClients() {
        return clientRepository.findAll().flatMap(clientEntity -> personRepository.findById(clientEntity.getPersonId())
                        .map(personEntity -> ClientDto.builder().address(personEntity.getAddress()).name(personEntity.getName())
                                .phone(personEntity.getPhone()).password(clientEntity.getPassword())
                                .status(clientEntity.getStatus() ? "Activo" : "Inactivo").build())
                )
                .doOnComplete(() -> logger.msgInfo(null, "La operación de obtención de clientes se completó exitosamente", null, null))
                .doOnError(error -> log.error("Ocurrió un error al obtener los clientes: {}", error.getMessage()))
                .doOnTerminate(() -> log.info("El flujo de obtención de clientes ha terminado"));
    }

    @Override
    public Mono<Client> getById(String id) {
        return repositoryServicePort.findClientById(id)
                .doOnSuccess(response -> log.info("getById finished successfully"))
                .doOnError(error -> log.error(
                        "service getById finished with error. ErrorDetail: {}", error.getMessage()
                ));
    }

    @Override
    public Flux<PersonEntity> getAllPerson() {
        return personRepository.findAll()
                .doOnComplete(() -> logger.msgInfo(null, "La operación de obtención de clientes se completó exitosamente", null, null))
                .doOnError(error -> log.error("Ocurrió un error al obtener todos los clientes clientes: {}", error.getMessage()))
                .doOnTerminate(() -> log.info("El flujo de obtención de todos clientes ha terminado"));
    }

    @Override
    public Mono<ResponseEntity<String>> registerClient(NewClient newClient) {
        PersonEntity personEntityRegister = new PersonEntity();
        ClientEntity clientEntityRegister = new ClientEntity();
        String passwordEncrypt = "";

        personEntityRegister.setAddress(newClient.getAddress());
        personEntityRegister.setAge(newClient.getAge());
        personEntityRegister.setGender(newClient.getGender());

        personEntityRegister.setIdentification(newClient.getIdentification());
        personEntityRegister.setName(newClient.getName());
        personEntityRegister.setPhone(newClient.getPhone());

        passwordEncrypt = aes256.toAES256(newClient.getPassword());
        clientEntityRegister.setPassword(passwordEncrypt);
        clientEntityRegister.setStatus(true);

        return personRepository.findByIdentification(newClient.getIdentification())
                .flatMap(existingClient -> {
                    return Mono.error(new BusinessLogicException("Cliente con identificacion: "
                            + newClient.getIdentification() + " ya existente",
                            "Cliente ya existente"));
                })
                .switchIfEmpty(personRepository.save(personEntityRegister).flatMap(savedPersonEntity -> {
                    clientEntityRegister.setPersonId(savedPersonEntity.getIdPerson());
                    return clientRepository.save(clientEntityRegister);
                }))
                .doOnSuccess(savedClient -> {
                    logger.msgInfo(null, "Cliente Registrado", null, null);
                })
                .doOnError(e -> {
                    logger.buildError(getClass().getName(), "save",
                            "Error en el registro del cliente",
                            e.getMessage(), HttpStatus.BAD_REQUEST.toString());
                })
                .then(Mono.just(new ResponseEntity<>("Cliente Registrado!", HttpStatus.CREATED)));
    }

    @Override
    public Mono<ResponseEntity<String>> updateClient(Long id, UpdateClient updateClient) {
        PersonEntity personEntityToUpdate = new PersonEntity();
        ClientEntity clientEntityToUpdate = new ClientEntity();
        String passwordEncrypt = "";

        personEntityToUpdate.setAddress(updateClient.getAddress());
        personEntityToUpdate.setAge(updateClient.getAge());
        personEntityToUpdate.setGender(updateClient.getGender());
        personEntityToUpdate.setName(updateClient.getName());
        personEntityToUpdate.setPhone(updateClient.getPhone());

        // Si la contraseña fue proporcionada, se encripta
        if (updateClient.getPassword() != null && !updateClient.getPassword().isEmpty()) {
            passwordEncrypt = aes256.toAES256(updateClient.getPassword());
            clientEntityToUpdate.setPassword(passwordEncrypt);
        }

        if (updateClient.getStatus() != null) {
            clientEntityToUpdate.setStatus(updateClient.getStatus());
        }

        return clientRepository.findById(id)
                .flatMap(existingClientEntity -> {
                    // Si el cliente existe, se actualizan sus datos
                    return personRepository.findById(existingClientEntity.getPersonId())
                            .flatMap(existingPersonEntity -> {
                                // Actualizar la persona asociada
                                existingPersonEntity.setAddress(personEntityToUpdate.getAddress());
                                existingPersonEntity.setAge(personEntityToUpdate.getAge());
                                existingPersonEntity.setGender(personEntityToUpdate.getGender());
                                existingPersonEntity.setName(personEntityToUpdate.getName());
                                existingPersonEntity.setPhone(personEntityToUpdate.getPhone());

                                return personRepository.save(existingPersonEntity)
                                        .flatMap(updatedPersonEntity -> {
                                            existingClientEntity.setPassword(clientEntityToUpdate.getPassword() != null ?
                                                    clientEntityToUpdate.getPassword() : existingClientEntity.getPassword());
                                            existingClientEntity.setStatus(clientEntityToUpdate.getStatus());
                                            return clientRepository.save(existingClientEntity);
                                        });
                            });


                })
                .switchIfEmpty(Mono.error(new BusinessLogicException("Cliente con ID: " + id + " no encontrado",
                        "Cliente no encontrado")))
                .doOnSuccess(updatedClientEntity -> {
                    logger.msgInfo(null, "Cliente Actualizado", null, null);
                })
                .doOnError(e -> {
                    logger.buildError(getClass().getName(), "update",
                            "Error en la actualización del cliente", e.getMessage(),
                            HttpStatus.BAD_REQUEST.toString());
                })
                .then(Mono.just(new ResponseEntity<>("Cliente Actualizado!", HttpStatus.OK)));
    }

    public Mono<ResponseEntity<String>> deleteClient(Long idClient) {
        return clientRepository.findById(idClient)
                .flatMap(existingClientEntity -> {
                    return personRepository.findById(existingClientEntity.getPersonId())
                            .flatMap(existingPersonEntity -> {
                                return clientRepository.deleteById(existingClientEntity.getIdClient())
                                        .then(personRepository.deleteById(existingPersonEntity.getIdPerson()))
                                        .then(Mono.just(new ResponseEntity<>("Cliente y Persona eliminados", HttpStatus.OK)));
                            })
                            .switchIfEmpty(Mono.error(new BusinessLogicException("Persona asociada no encontrada para el cliente con ID: " + idClient,
                                    "Persona no encontrada")));
                })
                .switchIfEmpty(Mono.error(new BusinessLogicException("Cliente con ID: " + idClient + " no encontrado", "Cliente no encontrado")))
                .doOnSuccess(aVoid -> {
                    logger.msgInfo(null, "Cliente y Persona eliminados", null, null);
                })
                .doOnError(e -> {
                    logger.buildError(getClass().getName(), "delete",
                            "Error en la eliminación del cliente", e.getMessage(),
                            HttpStatus.BAD_REQUEST.toString());
                });

    }


}
