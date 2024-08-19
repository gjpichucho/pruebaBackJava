package com.nttdata.movementsservice.service.imp;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.nttdata.movementsservice.exception.BusinessLogicException;
import com.nttdata.movementsservice.exception.DataAccessCustomException;
import com.nttdata.movementsservice.exception.ModelNotFoundException;
import com.nttdata.movementsservice.exception.NotFoundException;
import com.nttdata.movementsservice.model.dto.MovementDto;
import com.nttdata.movementsservice.model.dto.NewMovement;
import com.nttdata.movementsservice.model.dto.UpdateMovement;
import com.nttdata.movementsservice.model.entities.Account;
import com.nttdata.movementsservice.model.entities.Movement;
import com.nttdata.movementsservice.repository.AccountRepository;
import com.nttdata.movementsservice.repository.MovementRepository;
import com.nttdata.movementsservice.service.ClientService;
import com.nttdata.movementsservice.service.IMovementService;



@Service
public class MovementServiceImp implements IMovementService {
	
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MovementRepository movementRepository;
    
	@Autowired
	private ClientService clientService;

    @Autowired
    private LoggerService logger;

    @Override
    public ResponseEntity<Movement> creaMovement(NewMovement newMovement) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(newMovement.getAccountNumber());
        if (!optionalAccount.isPresent()) {
            throw new NotFoundException("Número de cuenta: " + newMovement.getAccountNumber() + " no encontrada",
                    " cuenta no encontrada, proporcione una cuenta correcta", HttpStatus.BAD_REQUEST);
        }
        Movement movement = new Movement();
        movement.setAccount(optionalAccount.get());
        if (newMovement.getValue().compareTo(BigDecimal.ZERO) == 0) {
            throw new BusinessLogicException(null, "El valor del movimiento no puede ser cero",
                    "Valor del movimiento invalido", null, null);
        }

        List<Movement> movements = movementRepository.findByAccountOrderByIdDesc(optionalAccount.get());
        
        if(newMovement.getValue().signum() == 1) {
            movement.setTypeMovement(TypeMovement.CREDITO);
            movement.setValue(newMovement.getValue());
            movement.setBalance(newMovement.getValue().add(movements.get(0).getBalance()));
            movement.setDateMovement(new Date());
        } else {
            movement.setTypeMovement(TypeMovement.DEBITO);
        	if (movements.get(0).getBalance().subtract(newMovement.getValue().abs()).signum() == -1) {
                throw new BusinessLogicException(null,
                        "Saldo no Disponible. El valor del movimiento no puede ser superior al del saldo actual",
                        "Valor del movimiento invalido. Saldo Actual es: ".concat(movements.get(0).getBalance().toString()), null, null);
            }

            movement.setValue(newMovement.getValue());
            movement.setBalance(movements.get(0).getBalance().subtract(newMovement.getValue()));
            movement.setDateMovement(new Date());
        }
        
        try {
            movementRepository.save(movement);
            logger.msgInfo(null, "Movimiento registrado", null, null);
        } catch (DataAccessException e) {
            logger.buildError(getClass().getName(), "save", "Error en el registro del movimiento", e.getMessage(),
                    HttpStatus.BAD_REQUEST.toString());
            throw new DataAccessCustomException("Error en el registro del movimiento. DataAccess", e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(movement, HttpStatus.OK);
    }
    
    @Override
    public ResponseEntity<String> updatMovement(Long id, UpdateMovement updateMovement) {
        Movement movementTemp = this.getById(id);
        if (movementTemp == null) {
            throw new ModelNotFoundException(String.format("Movimiento con id: %d no encontrado", id),
                    "El movimiento no existe", HttpStatus.NOT_FOUND);
        }
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(updateMovement.getAccountNumber());
        if (!optionalAccount.isPresent()) {
            throw new NotFoundException("Número de cuenta: " + updateMovement.getAccountNumber() + " no encontrada",
                    " cuenta no encontrada, proporcione una cuenta correcta", HttpStatus.BAD_REQUEST);
        }
        movementTemp.setAccount(optionalAccount.get());
        if (updateMovement.getTypeMovement() != null || updateMovement.getTypeMovement().equals("")) {
            movementTemp.setTypeMovement(updateMovement.getTypeMovement());
        }
        if (updateMovement.getDateMovement() != null) {
            movementTemp.setDateMovement(updateMovement.getDateMovement());
        }
        List<Movement> movements = movementRepository.findByAccountOrderByIdDesc(optionalAccount.get());
        if (updateMovement.getTypeMovement().equals(TypeMovement.CREDITO)) {
            if (updateMovement.getValue() != null) {
                if (updateMovement.getValue().compareTo(BigDecimal.ZERO) == 0
                        || updateMovement.getValue().signum() == -1) {
                    throw new BusinessLogicException(null, "El valor del movimiento no puede ser cero o negativo",
                            "Valor del movimiento invalido", null, null);
                }
                movementTemp.setValue(updateMovement.getValue());
                movementTemp.setBalance(updateMovement.getValue().add(movements.get(0).getBalance()));
            }

        } else {
            if (updateMovement.getValue() != null) {
                if (movements.get(0).getBalance().subtract(updateMovement.getValue()).signum() == -1) {
                    throw new BusinessLogicException(null,
                            "Saldo no Disponible. El valor del movimiento no puede ser superior al del saldo actual",
                            "Valor del movimiento invalido", null, null);
                }
                movementTemp.setValue(updateMovement.getValue());
                movementTemp.setBalance(movements.get(0).getBalance().subtract(updateMovement.getValue()));
            }
        }
        try {
            movementRepository.save(movementTemp);
            logger.msgInfo(null, "Movimiento editado", null, null);
        } catch (DataAccessException e) {
            logger.buildError(getClass().getName(), "save", "Error al editar el movimiento", e.getMessage(),
                    HttpStatus.BAD_REQUEST.toString());
            throw new DataAccessCustomException("Error al editar el movimiento. DataAccess", e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Movimiento Bancario Modificado!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteMovement(Long idMovement) {
        Movement optionalMovement = this.getById(idMovement);
        if (optionalMovement == null) {
            throw new ModelNotFoundException(String.format("Movimiento con id: %d no encontrado", idMovement),
                    "El movimiento que desea eliminar no existe", HttpStatus.NOT_FOUND);
        }
        try {
            movementRepository.deleteById(idMovement);
            logger.msgInfo(null, "Movimiento elimninado", null, null);
        } catch (DataAccessException e) {
            logger.buildError(getClass().getName(), "delete", "Error al eliminar el movimiento", e.getMessage(),
                    HttpStatus.BAD_REQUEST.toString());
            throw new DataAccessCustomException("Error al eliminar el movimiento. DataAccess", e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Movimiento Bancario Eliminado!", HttpStatus.OK);
    }

    @Override
    public List<MovementDto> getAllMovementsByAccount(Long idAccount) {
        Optional<Account> optionalAccount = accountRepository.findById(idAccount);
        if (!optionalAccount.isPresent()) {
            throw new ModelNotFoundException(
                    String.format("Cuenta con id: %d no encontrada", idAccount),
                    "La cuenta no existe", HttpStatus.NOT_FOUND);
        }
        List<Movement> movements = movementRepository.findByAccountOrderByIdDesc(optionalAccount.get());
        List<MovementDto> response = new ArrayList<>();
        for (Movement movement : movements) {
            MovementDto temp = MovementDto.builder().balance(movement.getBalance())
                    .dateMovement(movement.getDateMovement()).initialBalance(movement.getAccount().getInitialBalance())
                    .numberAccount(movement.getAccount().getAccountNumber())
                    .typeAccount(movement.getAccount().getTypeAccount().name())
                    .typeMovement(movement.getTypeMovement().name())
                    .status(movement.getAccount().getStatus() ? "Activa" : "Inactiva")
                    .valueMovement(movement.getValue())
                    .clientName(clientService.getClientById(movement.getAccount().getClientId()).getName()).build();
            response.add(temp);
        }
        return response;
    }

    @Override
    public Movement getById(Long id) {
        Optional<Movement> optionalMovement = movementRepository.findById(id);
        if (!optionalMovement.isPresent()) {
            throw new NotFoundException("Movimiento con Id: " + id + " no encontrada",
                    " Movimiento no encontrada, proporcione un id correcto", HttpStatus.BAD_REQUEST);
        }
        return optionalMovement.get();
    }

    @Override
    public List<MovementDto> getAllMovementsByRangeDate(List<Movement> listMovement) {
        List<MovementDto> response = new ArrayList<>();
        for (Movement movement : listMovement) {
            MovementDto temp = MovementDto.builder().balance(movement.getBalance())
                    .dateMovement(movement.getDateMovement()).initialBalance(movement.getAccount().getInitialBalance())
                    .numberAccount(movement.getAccount().getAccountNumber())
                    .typeAccount(movement.getAccount().getTypeAccount().name())
                    .typeMovement(movement.getTypeMovement().name())
                    .status(movement.getAccount().getStatus() ? "Activa" : "Inactiva")
                    .valueMovement(movement.getValue())
                    .clientName(clientService.getClientById(movement.getAccount().getClientId()).getName()).build();
            response.add(temp);
        }
        return response;
    }
    
    @Override
    public List<MovementDto> getAllMovementsByAccountAndDate(Long idAccount, String initialDate, String endDate) {
        Optional<Account> optionalAccount = accountRepository.findById(idAccount);
        if (!optionalAccount.isPresent()) {
            throw new ModelNotFoundException(
                    String.format("Cuenta con id: %d no encontrada", idAccount),
                    "La cuenta no existe", HttpStatus.NOT_FOUND);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateInitial = null;
        Date dateEnd = null;
		try {
			dateInitial = formatter.parse(initialDate);
			dateEnd = formatter.parse(endDate);
		} catch (ParseException e) {
			logger.buildError("", "error al parsear la fecha", e.toString(), "500");
			e.printStackTrace();
		}
 
        List<Movement> movements = movementRepository.findByAccountAndDateMovementBetweenOrderByIdDesc(optionalAccount.get(), dateInitial, dateEnd);
        List<MovementDto> response = new ArrayList<>();
        for (Movement movement : movements) {
            MovementDto temp = MovementDto.builder().balance(movement.getBalance())
                    .dateMovement(movement.getDateMovement()).initialBalance(movement.getAccount().getInitialBalance())
                    .numberAccount(movement.getAccount().getAccountNumber())
                    .typeAccount(movement.getAccount().getTypeAccount().name())
                    .typeMovement(movement.getTypeMovement().name())
                    .status(movement.getAccount().getStatus() ? "Activa" : "Inactiva")
                    .valueMovement(movement.getValue())
                    .clientName(clientService.getClientById(movement.getAccount().getClientId()).getName()).build();
            response.add(temp);
        }
        return response;
    }

}
