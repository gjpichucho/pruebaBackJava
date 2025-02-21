package com.nttdata.accountservice.infrastructure.input.adapter.rest.error;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.accountservice.infrastructure.exception.BussinessValidException;
import com.nttdata.accountservice.infrastructure.exception.FailureException;
import com.nttdata.accountservice.infrastructure.exception.ModelNotFoundException;
import com.nttdata.accountservice.infrastructure.exception.NotFoundException;
import com.nttdata.accountservice.infrastructure.input.adapter.rest.error.resolver.*;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class ErrorResolverHandler implements ErrorWebExceptionHandler {
    @Value("${info.project.version}")
    private String version;
    private final ObjectMapper mapper;
    private final UnexpectedErrorResolver unexpectedErrorResolver = new UnexpectedErrorResolver();
    private final NotFoundErrorResolver notFoundErrorResolver = new NotFoundErrorResolver();
    private final Map<Class<? extends Throwable>, ErrorResolver<?>> resolvers = new HashMap<>();

    @PostConstruct
    private void initializeResolvers() {
        resolvers.put(IllegalArgumentException.class, new IllegalArgumentExceptionResolver());
        resolvers.put(MissingRequestValueException.class, new MissingRequestValueExceptionResolver());
        resolvers.put(MethodArgumentNotValidException.class, new ConstraintViolationExceptionResolver());
        resolvers.put(ConstraintViolationException.class, new ConstraintViolationExceptionResolver());
        resolvers.put(WebExchangeBindException.class, new WebExchangeBindExceptionResolver());
        resolvers.put(ModelNotFoundException.class, notFoundErrorResolver);
        resolvers.put(NotFoundException.class, notFoundErrorResolver);
        resolvers.put(BussinessValidException.class, new ConflictExceptionResolver());

    }

    @NonNull
    private static Class<?> getThrowableClass(@NonNull final Throwable throwable, @NonNull final Class<?>... classes) {
        return Arrays.stream(classes)
                .filter(theClass -> theClass.isInstance(throwable))
                .findFirst()
                .orElse(throwable.getClass());
    }

    @NonNull
    @Override
    public Mono<Void> handle(@NonNull final ServerWebExchange serverWebExchange, @NonNull final Throwable throwable) {
        final var response = serverWebExchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        log.error("Error: " + throwable.getMessage());
        return Mono.just(resolvers.getOrDefault(
                        getThrowableClass(throwable, FailureException.class),
                        unexpectedErrorResolver))
                .flatMap(resolver ->
                        response.writeWith(
                                Mono.fromCallable(() -> mapper.writeValueAsBytes(resolver
                                                .apply(serverWebExchange, throwable, version)))
                                        .map(response.bufferFactory()::wrap))
                );
    }
}
