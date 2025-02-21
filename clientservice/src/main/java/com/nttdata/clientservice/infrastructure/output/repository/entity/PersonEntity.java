package com.nttdata.clientservice.infrastructure.output.repository.entity;

import com.nttdata.clientservice.infrastructure.enums.GenderType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "person")
public class PersonEntity {

    @Id
    @Column(value = "id_person")
    private Long idPerson;

    @Column(value = "name")
    private String name;

    @Column(value = "gender")
    private GenderType gender;

    @Column(value = "age")
    private Integer age;

    @Column(value = "identification")
    private String identification;

    @Column(value = "address")
    private String address;

    @Column(value = "phone")
    private String phone;

    // @OneToOne(mappedBy = "person")
    // private Client client;
}
