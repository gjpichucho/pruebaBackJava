package com.nttdata.movementsservice.service.model;

import lombok.Data;

@Data
public class Person {

	private Long id;

	private String name;

	private String gender;

	private Integer age;

	private String identification;

	private String address;

	private String phone;

}
