package com.nttdata.movementsservice.service;

import com.nttdata.movementsservice.model.dto.ReportDto;

public interface IReportService {
	
    /**
     * retorna el estado de cuenta de un cliente
     * @param idClient
     * @param endDate 
     * @param initialDate 
     * @return
     */
	public ReportDto getStatusAccountByClient(Long idClient, String initialDate, String endDate);


}
