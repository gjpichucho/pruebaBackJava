package com.nttdata.accountservice.infrastructure.input.adapter.rest.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRequest {

    @NotBlank(message = "accountNumber cannot be empty or null.")
    String accountNumber;
    @NotBlank(message = "accountType cannot be empty or null.")
    String accountType;
    @NotNull(message = "openingBalance cannot be null.")
    BigDecimal initialBalance;
    Boolean status;
    @NotBlank(message = "clientId cannot be empty or null.")
    String clientId;

}
