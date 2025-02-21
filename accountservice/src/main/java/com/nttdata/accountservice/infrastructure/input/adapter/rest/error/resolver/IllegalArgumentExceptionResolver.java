package com.nttdata.accountservice.infrastructure.input.adapter.rest.error.resolver;

import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.ApiError;
import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.ErrorList;
import com.nttdata.accountservice.infrastructure.util.ErrorUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

import java.util.List;

public class IllegalArgumentExceptionResolver extends ErrorResolver<ApiError> {
    @Override
    protected int status() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @NonNull
    @Override
    protected ApiError buildError(
            @NonNull final String requestPath,
            @NonNull final Throwable throwable,
            @NonNull final String version
    ) {
        final var exception = (IllegalArgumentException) throwable;
        return new ApiError().builder()
                .title("Bad input")
                .detail("The input data is invalid")
                .errors(
                        List.of(
                                new ErrorList().builder()
                                        .message("Bad Request")
                                        .businessMessage(exception.getMessage()).build()
                        )
                )
                .instance(ErrorUtils.buildErrorCode(status()))
                .type(requestPath).build();
    }
}
