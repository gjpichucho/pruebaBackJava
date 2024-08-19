package com.nttdata.movementsservice.service;

import com.nttdata.movementsservice.service.model.Client;

public interface ClientService {

	/**
	 * Obtiene el cliente segun el id
	 * @param clientId id del cliente
	 * @return
	 */
	Client getClientById(Long clientId);

}
