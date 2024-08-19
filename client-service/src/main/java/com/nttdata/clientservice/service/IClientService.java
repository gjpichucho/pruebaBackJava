package com.nttdata.clientservice.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.nttdata.clientservice.model.dto.ClientDto;
import com.nttdata.clientservice.model.dto.NewClient;
import com.nttdata.clientservice.model.dto.UpdateClient;
import com.nttdata.clientservice.model.entities.Client;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public interface IClientService {
	
    /**
     * Registrar nuevo cliente
     * 
     * @param newClient
     */
    public ResponseEntity<String> registerClient(@RequestBody NewClient newClient);

    /**
     * Editar cliente
     * 
     * @param updateClient
     */
    public ResponseEntity<String> updateClient(@PathVariable("id") Long id, @RequestBody UpdateClient updateClient);

    /**
     * Eliminar cliente
     * 
     * @pathVar idClient
     */
    public ResponseEntity<String> deleteClient(@PathVariable("id") Long idClient);

    /**
     * Obtener todos los clientes
     * 
     */
    public List<ClientDto> getAllClients();

    /**
     * Obtener un cliente por su id
     * 
     * @pathVar idClient
     */
    public Client getById(Long id);

}
