package com.nttdata.accountservice.infrastructure.input.adapter.rest.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {

    String accountNumber;
    String accountType;
    BigDecimal initialBalance;
    Boolean status;
    String clientId;

}
