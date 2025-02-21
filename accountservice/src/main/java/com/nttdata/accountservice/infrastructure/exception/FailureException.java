package com.nttdata.accountservice.infrastructure.exception;

import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.ApiError;
import lombok.AccessLevel;
import lombok.Generated;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Generated
public class FailureException extends RuntimeException {

    final ApiError failure;
    final int errorCode;

    public FailureException(@NonNull final ApiError failure, final int errorCode) {
        super(failure.getDetail());
        this.failure = failure;
        this.errorCode = errorCode;
    }

}