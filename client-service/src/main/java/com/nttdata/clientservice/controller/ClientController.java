package com.nttdata.clientservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.clientservice.model.dto.ClientDto;
import com.nttdata.clientservice.model.dto.NewClient;
import com.nttdata.clientservice.model.dto.UpdateClient;
import com.nttdata.clientservice.model.entities.Client;
import com.nttdata.clientservice.service.IClientService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/client")
@Api(tags = "Cliente")
public class ClientController {
	
    @Autowired
    private IClientService clientService;

    @PostMapping("/register")
    @ApiOperation(value = "Registrar un nuevo cliente", notes = "<b>Ejemplo de envio</b><br> URL: http://localhost:9012/client/register"
            + "<br><b>Se debe ingresar todos los atributos pertinenetes al cliente (nombre, genero, edad, identificación, dirección, teléfono, contraseña y estado)</b>"
            + "<br>Genero: MASCULINO, FEMENINO, OTRO</b>")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Cliente registrado con exito"),
            @ApiResponse(code = 404, message = "No se puede registrar el cliente") })
    public ResponseEntity<String> register(@RequestBody NewClient newClient) {
        return clientService.registerClient(newClient);
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "Editar un cliente por su id", notes = "<b>Ejemplo de envio</b><br> URL: http://localhost:9012/client/update/{id}"
            + "<br><b>El parametro id corresponde al id del cliente")
    public ResponseEntity<String> updateUserById(@PathVariable("id") Long id, @RequestBody UpdateClient client) {
        return clientService.updateClient(id, client);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Eliminar un cliente por su id", notes = "<b>Ejemplo de envio</b><br> URL: http://localhost:9012/client/delete/{id}"
            + "<br><b>El parametro id corresponde al id del cliente")
    public ResponseEntity<String> delete(@PathVariable("id") Long idClient) {
        return clientService.deleteClient(idClient);
    }

    @GetMapping("/get/all")
    @ApiOperation(value = "Obtener todos los clientes", notes = "<b>Ejemplo de envio</b><br> URL: http://localhost:9012/client/all")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "lista de clientes recuperada con exito ") })
    public List<ClientDto> getAll() {
        return clientService.getAllClients();
    }

    @GetMapping("/get/{id}")
    @ApiOperation(value = "Obtener el cliente por su id", notes = "<b>Ejemplo de envio</b><br> URL: http://localhost:9012/client/{id}")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "lista de clientes recuperada con exito ") })
    public Client getById(@PathVariable("id") Long id) {
        return clientService.getById(id);
    }

}
