package com.nttdata.movementsservice.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovementDto {

    Date dateMovement;

    String clientName;

    String numberAccount;

    String typeAccount;

    String typeMovement;

    BigDecimal initialBalance;

    String status;

    BigDecimal valueMovement;

    BigDecimal balance;

}
