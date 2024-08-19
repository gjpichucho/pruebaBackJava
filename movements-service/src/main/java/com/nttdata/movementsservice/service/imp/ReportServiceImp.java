package com.nttdata.movementsservice.service.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.movementsservice.model.dto.AccountDto;
import com.nttdata.movementsservice.model.dto.MovementDto;
import com.nttdata.movementsservice.model.dto.ReportDto;
import com.nttdata.movementsservice.model.entities.Account;
import com.nttdata.movementsservice.service.IAccountService;
import com.nttdata.movementsservice.service.IMovementService;
import com.nttdata.movementsservice.service.IReportService;


@Service
public class ReportServiceImp implements IReportService{
	
	@Autowired
	private IAccountService accountService;
	
	@Autowired
	private IMovementService movementsService;

	@Override
	public ReportDto getStatusAccountByClient(Long idClient, String initialDate, String endDate) {
		List<Account> allAccountsByClient = accountService.getAllAccountsByClient(idClient);
		String clientName = allAccountsByClient.get(0).getClientName();
		ReportDto report = new ReportDto();
		List<AccountDto> listResult = new ArrayList<>();
		allAccountsByClient.forEach(account -> {
			List<MovementDto> movementsByAccount = movementsService.getAllMovementsByAccountAndDate(account.getId(), initialDate, endDate);
			AccountDto temp = AccountDto.builder().initialBalance(account.getInitialBalance())
                    .numberAccount(account.getAccountNumber())
                    .nameClient(clientName).status(account.getStatus() ? "Activa" : "Inactiva")
                    .typeAccount(account.getTypeAccount().name()).build();
			temp.setMovements(movementsByAccount);
			listResult.add(temp);
			report.setAccount(listResult);
		});
		report.setAccount(listResult);
		return report;
	}

}
