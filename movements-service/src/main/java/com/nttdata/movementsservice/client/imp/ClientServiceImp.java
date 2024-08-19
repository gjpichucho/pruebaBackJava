package com.nttdata.movementsservice.client.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nttdata.movementsservice.client.ClientServiceClient;
import com.nttdata.movementsservice.config.LoggerService;
import com.nttdata.movementsservice.exception.RestRequestException;
import com.nttdata.movementsservice.service.ClientService;
import com.nttdata.movementsservice.service.model.Client;

import feign.FeignException;

@Service
public class ClientServiceImp implements ClientService {
	
	@Autowired
	private ClientServiceClient clientFeignClient;
	
    @Autowired
    private LoggerService logger;
	
	@Override
    public Client getClientById(Long clientId) {
		Client client;
		try {
			client = clientFeignClient.getById(clientId);
		} catch (FeignException e) {
			logger.buildError(getClass().getName(), "createAccount", "Error en la api de client by id", e.getMessage(),
					String.valueOf(e.status()) );
			throw new RestRequestException("Error en la llamada a la api de Client", e.getMessage(),
					HttpStatus.valueOf(e.status()));
		}
		return client;
		
	}
	
	

}
