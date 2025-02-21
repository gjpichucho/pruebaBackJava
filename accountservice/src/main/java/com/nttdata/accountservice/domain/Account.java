package com.nttdata.accountservice.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Generated
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {

    Long accountId;
    String accountNumber;
    String accountType;
    BigDecimal initialBalance;
    String status;
    String clientId;
}

