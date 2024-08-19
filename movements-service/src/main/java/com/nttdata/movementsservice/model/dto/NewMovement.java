package com.nttdata.movementsservice.model.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NewMovement {

    @NotEmpty
    @ApiModelProperty(notes = "Valor del movimiento", required = true)
    private BigDecimal value;

    @NotEmpty
    @ApiModelProperty(notes = "Número de cuenta", required = true)
    private String accountNumber;
}
