package com.nttdata.movementsservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.movementsservice.model.dto.ReportDto;
import com.nttdata.movementsservice.service.IReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/reports")
@Api(tags = "Reportes")
public class ReportController {
	
    @Autowired
    private IReportService reportService;
	
    @GetMapping("/statusAccount/rangeDate/idClient/{idClient}")
    @ApiOperation(value = "Obtener todos los movimeintos de una cuenta por un rango de fecha", notes = "<b>Ejemplo de envio</b><br> URL: http://localhost:9012/movement/idAccount/{idAccount}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "lista de movimientos por cuenta recuperada con exito ") })
    public ReportDto getByRnageDateAndAccount(
            @PathVariable("idClient") Long idClient, @RequestParam String initialDate,
            @RequestParam String endDate) {
    	
        return reportService.getStatusAccountByClient(idClient, initialDate, endDate);
    }

}
