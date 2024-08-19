package com.nttdata.movementsservice.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.nttdata.movementsservice.model.dto.AccountDto;
import com.nttdata.movementsservice.model.dto.NewAccount;
import com.nttdata.movementsservice.model.dto.UpdateAccount;
import com.nttdata.movementsservice.model.entities.Account;

public interface IAccountService {

	/**
	 * Crear nueva cuenta
	 * 
	 * @param newAccount
	 */
	public ResponseEntity<Account> createAccount(@RequestBody NewAccount newAccount);

	/**
	 * Editar cuenta
	 * 
	 * @param updateClient
	 */
	public ResponseEntity<String> updateAccount(Long id, UpdateAccount updateAccount);

	/**
	 * Eliminar cuenta
	 * 
	 * @pathVar idAccount
	 */
	public ResponseEntity<String> deleteAccount(Long idAccount);

	/**
	 * Obtener todas las cuentas
	 * 
	 */
	public List<AccountDto> getAllAccounts();

	/**
	 * Obtener todas las cuentas de un cliente
	 * 
	 * @pathVariable idClient
	 * 
	 */
	public List<Account> getAllAccountsByClient(Long idClient);

	/**
	 * Obtener cuenta por Id
	 * 
	 * @pathVariable idAccount
	 * 
	 */
	public Account getById(Long idClient);

}
