package com.nttdata.clientservice.domain;

import com.nttdata.clientservice.infrastructure.enums.GenderType;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateClient {

    @NotEmpty
    private String name;

    private GenderType gender;

    private Integer age;

    @NotEmpty
    private String address;

    private String phone;

    @NotEmpty
    private String password;

    @NotEmpty
    private Boolean status;

}
