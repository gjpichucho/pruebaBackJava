package com.nttdata.movementsservice.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.nttdata.movementsservice.model.dto.MovementDto;
import com.nttdata.movementsservice.model.dto.NewMovement;
import com.nttdata.movementsservice.model.dto.UpdateMovement;
import com.nttdata.movementsservice.model.entities.Movement;


public interface IMovementService {
	
    /**
     * Crear nuevo movimiento
     * 
     * @param newAccount
     */
    public ResponseEntity<Movement> creaMovement(NewMovement newAccount);
    
    /**
     * Editar movimiento
     * 
     * @param updateMovement
     */
    public ResponseEntity<String> updatMovement(Long id,
            @RequestBody UpdateMovement updateMovement);

    /**
     * Eliminar movimiento
     * 
     * @pathVar idMovement
     */
    public ResponseEntity<String> deleteMovement(Long idMovement);

    /**
     * Obtener todas los movimientos de una cuenta
     * 
     * @pathVar idAccount
     * 
     */
    public List<MovementDto> getAllMovementsByAccount(Long idAccount);

    /**
     * Obtener todas los movimientos de una cuenta por un rango de fecha
     * 
     * @pathVar idAccount
     * 
     */
    public List<MovementDto> getAllMovementsByRangeDate(List<Movement> listMovement);

    /**
     * Obtener movimiento de una cuenta por su id
     * 
     * @pathVar idMovement
     * 
     */
    public Movement getById(Long idMovement);
    
    /**
     * Obtener todas los movimientos de una cuenta por fecha
     * @param idAccount
     * @param initialDate
     * @param endDate
     * @return
     */
	List<MovementDto> getAllMovementsByAccountAndDate(Long idAccount, String initialDate, String endDate);

}
