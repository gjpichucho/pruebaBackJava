package com.nttdata.movementsservice.service.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nttdata.movementsservice.config.LoggerService;
import com.nttdata.movementsservice.enums.TypeMovement;
import com.nttdata.movementsservice.exception.DataAccessCustomException;
import com.nttdata.movementsservice.exception.ModelNotFoundException;
import com.nttdata.movementsservice.exception.NotFoundException;
import com.nttdata.movementsservice.model.dto.AccountDto;
import com.nttdata.movementsservice.model.dto.NewAccount;
import com.nttdata.movementsservice.model.dto.UpdateAccount;
import com.nttdata.movementsservice.model.entities.Account;
import com.nttdata.movementsservice.model.entities.Movement;
import com.nttdata.movementsservice.repository.AccountRepository;
import com.nttdata.movementsservice.repository.MovementRepository;
import com.nttdata.movementsservice.service.ClientService;
import com.nttdata.movementsservice.service.IAccountService;
import com.nttdata.movementsservice.service.model.Client;

@Service
public class AccountServiceImp implements IAccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private MovementRepository movementRepository;
	
	@Autowired
	private ClientService clientService;

	@Autowired
	private LoggerService logger;

	@Override
	public ResponseEntity<Account> createAccount(NewAccount newAccount) {
		Account registerAccount = new Account();
		Account initialMovementAcount = new Account();
		Movement movement = new Movement();
		Client client = clientService.getClientById(newAccount.getIdClient());
		
		registerAccount.setAccountNumber(newAccount.getAccountNumber());
		registerAccount.setInitialBalance(newAccount.getInitialBalance());
		registerAccount.setStatus(newAccount.getStatus());
		registerAccount.setTypeAccount(newAccount.getTypeAccount());
		registerAccount.setClientId(client != null ? client.getId() : null);
		movement.setBalance(newAccount.getInitialBalance());
		movement.setDateMovement(new Date());
		movement.setTypeMovement(TypeMovement.CREDITO);
		movement.setValue(newAccount.getInitialBalance());
		try {
			initialMovementAcount = accountRepository.save(registerAccount);
			movement.setAccount(initialMovementAcount);
			movementRepository.save(movement);
			logger.msgInfo(null, "Cuenta registrada", null, null);
		} catch (DataAccessException e) {
			logger.buildError(getClass().getName(), "save", "Error en el registro de la cuenta", e.getMessage(),
					HttpStatus.BAD_REQUEST.toString());
			throw new DataAccessCustomException("Error en el registro de la cuenta. DataAccess", e.getMessage(),
					HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(registerAccount, HttpStatus.OK);
	}
	
    @Override
    public ResponseEntity<String> updateAccount(Long id, UpdateAccount updateAccount) {
        Account accountTemp = this.getById(id);
        if (accountTemp == null) {
            throw new ModelNotFoundException(String.format("Cuenta con id: %d no encontrado", id),
                    "La cuenta no existe", HttpStatus.NOT_FOUND);
        }

        if (updateAccount.getAccountNumber() != null && !updateAccount.getAccountNumber().equals("")) {
            accountTemp.setAccountNumber(updateAccount.getAccountNumber());
        }
        if (updateAccount.getStatus() != null) {
            accountTemp.setStatus(updateAccount.getStatus());
        }
        if (updateAccount.getTypeAccount() != null && !updateAccount.getTypeAccount().equals("")) {
            accountTemp.setTypeAccount(updateAccount.getTypeAccount());
        }
        if (updateAccount.getIdClient() != null) {
            Client client = clientService.getClientById(updateAccount.getIdClient());
            if (client == null) {
                throw new ModelNotFoundException(
                        String.format("Cliente con id: %d no encontrado", updateAccount.getIdClient()),
                        "El cliente no existe", HttpStatus.NOT_FOUND);
            }
            accountTemp.setClientId(client.getId());
        }

        try {
            accountRepository.save(accountTemp);
            logger.msgInfo(null, "Cuenta editada", null, null);
        } catch (DataAccessException e) {
            logger.buildError(getClass().getName(), "update", "Error al editar la cuenta", e.getMessage(),
                    HttpStatus.BAD_REQUEST.toString());
            throw new DataAccessCustomException("Error al modificar la cuenta. DataAccess", e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Cuenta Modificada!", HttpStatus.OK);
    }
    
    @Override
    public ResponseEntity<String> deleteAccount(Long idAccount) {
        Account optionalAccount = this.getById(idAccount);
        if (optionalAccount == null) {
            throw new ModelNotFoundException(String.format("Cuenta con id: %d no encontrada", idAccount),
                    "La cuenta que desea eliminar no existe", HttpStatus.NOT_FOUND);
        }
        try {
            accountRepository.deleteById(idAccount);
            logger.msgInfo(null, "Cuenta elimninada", null, null);
        } catch (DataAccessException e) {
            logger.buildError(getClass().getName(), "delete", "Error al eliminar la cuenta", e.getMessage(),
                    HttpStatus.BAD_REQUEST.toString());
            throw new DataAccessCustomException("Error al eliminar la cuenta. DataAccess", e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Cuenta Eliminada!", HttpStatus.OK);
    }
    
    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        List<AccountDto> response = new ArrayList<>();
        for (Account account : accounts) {
            AccountDto temp = AccountDto.builder().initialBalance(account.getInitialBalance())
                    .numberAccount(account.getAccountNumber())
                    .nameClient(clientService.getClientById(account.getClientId()).getName())
                    .status(account.getStatus() ? "Activa" : "Inactiva")
                    .typeAccount(account.getTypeAccount().name()).build();
            response.add(temp);
        }
        return response;
    }
    
    @Override
    public List<Account> getAllAccountsByClient(Long idClient) {
        Client client = clientService.getClientById(idClient);
        if (client == null) {
            throw new ModelNotFoundException(
                    String.format("Cliente con id: %d no encontrado", idClient),
                    "El cliente no existe", HttpStatus.NOT_FOUND);
        }
        List<Account> accounts = accountRepository.findByClientId(idClient);
        accounts.get(0).setClientName(client.getName());
        return accounts;
    }

    @Override
    public Account getById(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (!optionalAccount.isPresent()) {
            throw new NotFoundException("Cuenta con Id: " + id + " no encontrada",
                    " cuenta no encontrada, proporcione un id correcto", HttpStatus.BAD_REQUEST);
        }
        return optionalAccount.get();
    }


}
