package com.nttdata.accountservice.infrastructure.input.adapter.rest.error.resolver;

import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.ApiError;
import com.nttdata.accountservice.infrastructure.util.ErrorUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebExchangeBindException;

public class WebExchangeBindExceptionResolver extends ErrorResolver<ApiError> {
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
    final var exception = (WebExchangeBindException) throwable;
    return new ApiError().builder()
      .title("Bad input")
      .detail(exception.getReason())
      .errors(ErrorUtils.getErrors(exception))
      .instance(ErrorUtils.buildErrorCode(status()))
      .type(requestPath).build();
  }
}
