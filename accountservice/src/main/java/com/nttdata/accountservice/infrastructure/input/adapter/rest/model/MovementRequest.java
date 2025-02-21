package com.nttdata.accountservice.infrastructure.input.adapter.rest.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovementRequest {
    private String accountId;

    private BigDecimal amount;

    private String movementType;
}
