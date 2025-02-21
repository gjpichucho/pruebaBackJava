package com.nttdata.accountservice.infrastructure.input.adapter.rest.error.resolver;

import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.ApiError;
import com.nttdata.accountservice.infrastructure.util.ErrorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

@Slf4j
public class UnexpectedErrorResolver extends ErrorResolver<ApiError>{

    @Override
    protected int status() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    @NonNull
    @Override
    protected ApiError buildError(@NonNull final String requestPath,
                                    @NonNull final Throwable throwable,
                                    @NonNull final String version) {

        return new ApiError().builder()
                .title("UNEXPECTED ERROR")
                .detail("An unexpected error has occurred")
                .instance(ErrorUtils.buildErrorCode(status()))
                .type(requestPath).build();
    }
}
