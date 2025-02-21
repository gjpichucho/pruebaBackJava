package com.nttdata.clientservice.infrastructure.output.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "client")
public class ClientEntity {

    @Id
    @Column(value = "id_client")
    private Long idClient;

    @Column(value = "id_person")
    private Long personId;

    @Column(value = "password")
    private String password;

    @Column(value = "status")
    private Boolean status;

}
