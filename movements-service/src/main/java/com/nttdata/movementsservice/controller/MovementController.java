package com.nttdata.movementsservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.movementsservice.model.dto.MovementDto;
import com.nttdata.movementsservice.model.dto.NewMovement;
import com.nttdata.movementsservice.model.dto.UpdateMovement;
import com.nttdata.movementsservice.model.entities.Movement;
import com.nttdata.movementsservice.repository.MovementRepository;
import com.nttdata.movementsservice.service.IMovementService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.kaczmarzyk.spring.data.jpa.domain.Between;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

@RestController
@RequestMapping("/movement")
@Api(tags = "Movimiento")
public class MovementController {

    @Autowired
    private IMovementService movementService;

    @Autowired
    private MovementRepository movementRepository;

    @PostMapping("/create")
    @ApiOperation(value = "Crear un movimiento", notes = "<b>Ejemplo de envio</b><br> URL: http://localhost:9012/movement/create"
            + "<br><b>Se debe ingresar todos los atributos pertinenetes al movimiento (tipo de Movimiento, valor, cuenta)</b>"
            + "<br> Tipo de Movimiento ->  CREDITO, DEBITO")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Movimiento registrado con exito"),
            @ApiResponse(code = 404, message = "No se puede registrar el cliente") })
    public ResponseEntity<Movement> register(@RequestBody NewMovement newMovement) {
        return movementService.creaMovement(newMovement);
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "Editar un movimiento", notes = "<b>Ejemplo de envio</b><br> URL: http://localhost:9012/movement/update/{id}"
            + "<br><b>El parametro id corresponde al id del movimiento")
    public ResponseEntity<String> updateUserById(@PathVariable("id") Long id, @RequestBody UpdateMovement account) {
        return movementService.updatMovement(id, account);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Eliminar un movimiento por su id", notes = "<b>Ejemplo de envio</b><br> URL: http://localhost:9012/account/movement/{id}"
            + "<br><b>El parametro id corresponde al id del movimiento")
    public ResponseEntity<String> delete(@PathVariable("id") Long idMovement) {
        movementService.deleteMovement(idMovement);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get/idAccount/{idAccount}")
    @ApiOperation(value = "Obtener todos los movimientos de una cuenta", notes = "<b>Ejemplo de envio</b><br> URL: http://localhost:9012/movement/idAccount/{idAccount}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "lista de movimientos por cuenta recuperada con exito ") })
    public List<MovementDto> getAllByAccount(@PathVariable("idAccount") Long idAccount) {
        return movementService.getAllMovementsByAccount(idAccount);
    }

    @GetMapping("/get/{id}")
    @ApiOperation(value = "Obtener un movimiento por su id", notes = "<b>Ejemplo de envio</b><br> URL: http://localhost:9012/movement/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Movimiento recuperado con exito ") })
    public Movement getById(@PathVariable("id") Long idMovement) {
        return movementService.getById(idMovement);
    }

    @GetMapping("/get/rangeDate/idAccount/{idAccount}")
    @ApiOperation(value = "Obtener todos los movimientos de una cuenta por un rango de fecha", notes = "<b>Ejemplo de envio</b><br> URL: http://localhost:9012/movement/idAccount/{idAccount}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "lista de movimientos por cuenta recuperada con exito ") })
    public List<MovementDto> getByRnageDateAndAccount(@Conjunction(value = {
            @Or({ @Spec(path = "idAccount", params = "idAccount", spec = Equal.class) }),
            @Or({ @Spec(path = "dateMovement", params = { "initialDate",
                    "endDate" }, config = "yyyy-MM-dd", spec = Between.class) }) }) Specification<Movement> movementSpec,
            @PathVariable("idAccount") Long idAccount, @RequestParam String initialDate,
            @RequestParam String endDate) {
        List<Movement> movements = movementRepository.findAll(movementSpec);
        return movementService.getAllMovementsByRangeDate(movements);
    }

}
