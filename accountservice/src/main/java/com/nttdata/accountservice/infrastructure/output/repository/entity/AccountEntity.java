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

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "account")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountEntity {
    @Id
    @Column("id_account")
    Long id;
    @Column("account_number")
    String accountNumber;
    @Column("account_type")
    String accountType;
    @Column("initial_balance")
    BigDecimal initialBalance;
    @Column("status")
    Boolean status;
    @Column("id_person")
    String clientId;

}
