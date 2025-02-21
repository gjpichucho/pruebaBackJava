package com.nttdata.accountservice.infrastructure.output.repository.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "movement")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovementEntity {

    @Id
    @Column("id_movement")
    Long id;
    @Column("id_account")
    String accountNumber;
    @Column("movement_date")
    LocalDateTime movementDate;
    @Column("movement_type")
    String movementType;
    @Column("amount")
    BigDecimal amount;
    @Column("balance")
    BigDecimal balance;
}
