package com.nttdata.accountservice.infrastructure.output.adapter;

import com.nttdata.accountservice.application.output.port.ClientServicePort;
import com.nttdata.accountservice.domain.Client;
import com.nttdata.accountservice.infrastructure.exception.BussinessValidException;
import com.nttdata.accountservice.infrastructure.output.adapter.config.ApiClientProperties;
import com.nttdata.accountservice.infrastructure.output.adapter.mapper.ClientServiceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceAdapter implements ClientServicePort {

    private final ApiClientProperties apiClientProperties;
    private final ClientServiceMapper clientServiceMapper;

    @Override
    public Mono<Client> findClientById(String customerId) {
        return WebClient.create(apiClientProperties.getClient().getBaseUrl())
                .get().uri("/client/get/{id}", customerId)
                .retrieve()
                .bodyToMono(com.nttdata.accountservice.infrastructure.output.client.model.Client.class)
                .map(clientServiceMapper::toClient)
                .onErrorMap(throwable -> new BussinessValidException(throwable.getMessage()))
                .doOnSuccess(response -> log.info("findCustomerById finished successfully"))
                .doOnError(error -> log.error("findCustomerById finished with error. ErrorDetail: {}",
                        error.getMessage()));

    }
}
