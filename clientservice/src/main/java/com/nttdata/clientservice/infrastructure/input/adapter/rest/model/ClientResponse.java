package com.nttdata.clientservice.infrastructure.input.adapter.rest.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientResponse {

    private String id;

    private String name;

    private String gender;

    private Integer age;

    private String address;

    private String phone;

    private String password;

    private String status;
}
