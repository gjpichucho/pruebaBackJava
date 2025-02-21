package com.nttdata.clientservice.domain;

import com.nttdata.clientservice.infrastructure.enums.GenderType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class NewClient {
	
	@NotEmpty
    private String name;

    @NotNull
    private GenderType gender;

    private Integer age;

    @NotEmpty
    private String identification;

    @NotEmpty
    private String address;

    private String phone;

    @NotEmpty
    private String password;

}
