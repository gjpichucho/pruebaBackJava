package com.nttdata.accountservice.infrastructure.output.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Client {

    private String id;

    private String name;

    private String gender;

    private Integer age;

    private String address;

    private String phone;

    private String password;

    private String status;
}
