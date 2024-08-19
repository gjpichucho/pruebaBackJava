package com.nttdata.movementsservice.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotEmpty;

import com.nttdata.movementsservice.enums.TypeMovement;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateMovement {

    @NotEmpty
    @ApiModelProperty(notes = "Fecha del movimiento", required = false)
    private Date dateMovement;

    @NotEmpty
    @ApiModelProperty(notes = "tipo de movimiento", required = false)
    private TypeMovement typeMovement;

    @NotEmpty
    @ApiModelProperty(notes = "Valor del movimiento", required = false)
    private BigDecimal value;

    @NotEmpty
    @ApiModelProperty(notes = "Número de cuenta", required = true)
    private String accountNumber;

}
