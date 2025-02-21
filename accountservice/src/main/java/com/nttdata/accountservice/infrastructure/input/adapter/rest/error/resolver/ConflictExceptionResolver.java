package com.nttdata.accountservice.infrastructure.input.adapter.rest.error.resolver;

import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.ApiError;
import com.nttdata.accountservice.infrastructure.util.ErrorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

@Slf4j
public class ConflictExceptionResolver extends ErrorResolver<ApiError> {

  @Override
  protected int status() {
    return HttpStatus.CONFLICT.value();
  }

  @NonNull
  @Override
  protected ApiError buildError(@NonNull String requestPath,
                                  @NonNull Throwable throwable,
                                  @NonNull String version) {
    return new ApiError().builder()
        .title("CONFLICT")
        .detail(throwable.getMessage())
        .type(requestPath)
        .instance(ErrorUtils.buildErrorCode(status())).build();
  }
}
