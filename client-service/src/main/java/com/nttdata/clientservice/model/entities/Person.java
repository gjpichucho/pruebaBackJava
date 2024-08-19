package com.nttdata.clientservice.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.nttdata.clientservice.model.enums.GenderType;

import lombok.Data;

@Data
@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_person")
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "gender", length = 10, nullable = true)
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Column(name = "age", length = 3, nullable = false)
    private Integer age;

    @Column(name = "identification", length = 16, nullable = false)
    private String identification;

    @Column(name = "address", length = 100, nullable = false)
    private String address;

    @Column(name = "phone", length = 12, nullable = true)
    private String phone;

    // @OneToOne(mappedBy = "person")
    // private Client client;
}
