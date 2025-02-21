package com.nttdata.accountservice.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Generated
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Movement {
    Long movementId;
    String accountNumber;
    LocalDateTime movementDate;
    String movementType;
    BigDecimal amount;
    BigDecimal balance;
}
