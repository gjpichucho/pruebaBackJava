package com.nttdata.movementsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MovementsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovementsServiceApplication.class, args);
	}

}
