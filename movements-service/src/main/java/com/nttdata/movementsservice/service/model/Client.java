package com.nttdata.movementsservice.service.model;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Client extends Person {

	private Long id;

	private String password;

	private Boolean status;

}
