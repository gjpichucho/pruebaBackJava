package com.nttdata.accountservice.infrastructure.input.adapter.rest.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorList {

    private String code;

    private String message;

    private String businessMessage;

    public ErrorList code(String code) {
        this.code = code;
        return this;
    }
}
