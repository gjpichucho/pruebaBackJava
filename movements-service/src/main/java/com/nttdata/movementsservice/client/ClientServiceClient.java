package com.nttdata.movementsservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nttdata.movementsservice.service.model.Client;

@FeignClient(url = "${apis.client}", name = "client-service")
@RequestMapping("/client")
public interface ClientServiceClient {
	
	@GetMapping("/get/{id}")
    public Client getById(@PathVariable("id") Long id);

}
