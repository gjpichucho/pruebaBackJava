package com.nttdata.clientservice.infrastructure.input.adapter.rest.impl;

import com.nttdata.clientservice.domain.ClientDto;
import com.nttdata.clientservice.domain.NewClient;
import com.nttdata.clientservice.domain.UpdateClient;
import com.nttdata.clientservice.application.input.port.IClientService;
import com.nttdata.clientservice.infrastructure.input.adapter.rest.mapper.ClientRestMapper;
import com.nttdata.clientservice.infrastructure.input.adapter.rest.model.ClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@RestController
@RequestMapping("/client")
public class ClientController {

    private final IClientService clientService;
    private final ClientRestMapper clientRestMapper;

    @GetMapping("/get/all")
    public Flux<ClientDto> getAll() {
        return clientService.getAllClients();
    }

    @GetMapping("/get/{id}")
    public Mono<ResponseEntity<ClientResponse>> getById(@PathVariable("id") String id) {
        return clientService.getById(id)
                .map(clientRestMapper::toClientResponse)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(@RequestBody NewClient newClient) {
        return  clientService.registerClient(newClient);
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<String>> updateUserById(@PathVariable("id") Long id, @RequestBody UpdateClient client) {
        return clientService.updateClient(id, client);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable("id") Long idClient) {
        return clientService.deleteClient(idClient);
    }



}
