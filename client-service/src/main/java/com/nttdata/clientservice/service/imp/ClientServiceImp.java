package com.nttdata.clientservice.service.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nttdata.clientservice.config.AES256;
import com.nttdata.clientservice.config.LoggerService;
import com.nttdata.clientservice.exception.BusinessLogicException;
import com.nttdata.clientservice.exception.DataAccessCustomException;
import com.nttdata.clientservice.exception.ModelNotFoundException;
import com.nttdata.clientservice.exception.NotFoundException;
import com.nttdata.clientservice.model.dto.ClientDto;
import com.nttdata.clientservice.model.dto.NewClient;
import com.nttdata.clientservice.model.dto.UpdateClient;
import com.nttdata.clientservice.model.entities.Client;
import com.nttdata.clientservice.repository.ClientRepository;
import com.nttdata.clientservice.service.IClientService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ClientServiceImp implements IClientService {

	//@Autowired
	private final ClientRepository clientRepository;

	@Autowired
	private LoggerService logger;

	@Autowired
	private AES256 aes256;

	@Override
	public ResponseEntity<String> registerClient(NewClient newClient) {
		Client clientRegister = new Client();
		String passwordEncrypt = "";
		
		Optional<Client> optionalClient = clientRepository.findByCi(newClient.getIdentification());
        if (optionalClient.isPresent()) {
            throw new BusinessLogicException("Cliente con identificacion: " + newClient.getIdentification() + " ya existente",
                    " Cliente ya existente");
        }
		clientRegister.setAddress(newClient.getAddress());
		clientRegister.setAge(newClient.getAge());
		clientRegister.setGender(newClient.getGender());

		clientRegister.setIdentification(newClient.getIdentification());
		clientRegister.setName(newClient.getName());
		clientRegister.setPhone(newClient.getPhone());

		passwordEncrypt = aes256.toAES256(newClient.getPassword());
		clientRegister.setPassword(passwordEncrypt);
		clientRegister.setStatus(true);
		try {
			clientRepository.save(clientRegister);
			logger.msgInfo(null, "Cliente Registrado", null, null);
		} catch (DataAccessException e) {
			logger.buildError(getClass().getName(), "save", "Error en el registro del cliente", e.getMessage(),
					HttpStatus.BAD_REQUEST.toString());
			throw new DataAccessCustomException("Error en el registro del cliente. DataAccess", e.getMessage(),
					HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("Cliente Registrado!", HttpStatus.CREATED);
	}
	
    @Override
    public ResponseEntity<String> updateClient(Long id, UpdateClient updateClient) {
        Client clientTemp = this.getById(id);
        if (clientTemp == null) {
            throw new ModelNotFoundException(String.format("Cliente con id: %d no encontrado", id),
                    "El cliente no existe", HttpStatus.NOT_FOUND);
        }
        String passwordEncrypt = "";
        if (updateClient.getAddress() != null &&
                !updateClient.getAddress().equals("")) {
            clientTemp.setAddress(updateClient.getAddress());
        }
        if (updateClient.getAge() != null) {
            clientTemp.setAge(updateClient.getAge());
        }
        if (updateClient.getGender() != null && !updateClient.getGender().equals("")) {
            clientTemp.setGender(updateClient.getGender());
        }
        if (updateClient.getName() != null && !updateClient.getName().equals("")) {
            clientTemp.setName(updateClient.getName());
        }
        if (updateClient.getPhone() != null && !updateClient.getPhone().equals("")) {
            clientTemp.setPhone(updateClient.getPhone());
        }
        if (updateClient.getPassword() != null &&
                !updateClient.getPassword().equals("")) {
            passwordEncrypt = aes256.toAES256(updateClient.getPassword());
            clientTemp.setPassword(passwordEncrypt);
        }
        if (updateClient.getStatus() != null) {
            clientTemp.setStatus(updateClient.getStatus());
        }

        try {
            clientRepository.save(clientTemp);
            logger.msgInfo(null, "Cliente editado", null, null);
        } catch (DataAccessException e) {
            logger.buildError(getClass().getName(), "update", "Error al editar el cliente", e.getMessage(),
                    HttpStatus.BAD_REQUEST.toString());
            throw new DataAccessCustomException("Error al modificar el cliente. DataAccess", e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Cliente Modificado!", HttpStatus.OK);
    }
    
    @Override
    public ResponseEntity<String> deleteClient(Long idClient) {
        Client optionalClient = this.getById(idClient);
        if (optionalClient == null) {
            throw new ModelNotFoundException(String.format("Cliente con id: %d no encontrado", idClient),
                    "El cliente que desea eliminar no existe", HttpStatus.NOT_FOUND);
        }
        try {
            clientRepository.deleteById(idClient);
            logger.msgInfo(null, "Cliente elimninado", null, null);
        } catch (DataAccessException e) {
            logger.buildError(getClass().getName(), "delete", "Error al eliminar el cliente", e.getMessage(),
                    HttpStatus.BAD_REQUEST.toString());
            throw new DataAccessCustomException("Error al eliminar el cliente. DataAccess", e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Cliente Eliminado!", HttpStatus.OK);
    }
    
    @Override
    public List<ClientDto> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        List<ClientDto> response = new ArrayList<>();
        for (Client client : clients) {
            ClientDto temp = ClientDto.builder().address(client.getAddress()).name(client.getName())
                    .phone(client.getPhone()).password(client.getPassword())
                    .status(client.getStatus() ? "Activo" : "Inactivo").build();
            response.add(temp);
        }
        return response;
    }
    
    @Override
    public Client getById(Long id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (!optionalClient.isPresent()) {
            throw new NotFoundException("Cliente con Id: " + id + " no encontrado",
                    " cliente no encontrado, proporcione un id correcto", HttpStatus.BAD_REQUEST);
        }
        return optionalClient.get();
    }

}
