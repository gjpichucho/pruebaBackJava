package com.nttdata.accountservice.infrastructure.input.adapter.rest.error.resolver;

import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.ApiError;
import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.ErrorList;
import com.nttdata.accountservice.infrastructure.util.ErrorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.MissingRequestValueException;

import java.util.List;

@Slf4j
public class MissingRequestValueExceptionResolver extends ErrorResolver<ApiError> {
    @Override
    protected int status() {
        return HttpStatus.CONFLICT.value();
    }

    @Override
    protected ApiError buildError(String requestPath, Throwable throwable, String version) {
        final var exception = (MissingRequestValueException) throwable;
        return new ApiError().builder()
                .title("Missing input")
                .detail("The input data is missing")
                .errors(
                        List.of(new ErrorList().builder().message(String.format("Bad Request: %s", exception.getName()))
                                .businessMessage(exception.getReason()).build())
                )
                .instance(ErrorUtils.buildErrorCode(exception.getStatusCode().value()))
                .type(requestPath).build();
    }
}
