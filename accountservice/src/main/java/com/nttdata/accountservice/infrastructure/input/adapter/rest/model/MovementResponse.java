package com.nttdata.accountservice.infrastructure.input.adapter.rest.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovementResponse {
    Long movementId;
    String accountNumber;
    LocalDateTime movementDate;
    String movementType;
    BigDecimal amount;
    BigDecimal balance;
}
