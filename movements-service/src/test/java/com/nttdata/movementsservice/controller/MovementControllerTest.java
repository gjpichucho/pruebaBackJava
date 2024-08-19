package com.nttdata.movementsservice.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.nttdata.movementsservice.enums.TypeAccount;
import com.nttdata.movementsservice.model.entities.Account;
import com.nttdata.movementsservice.service.IMovementService;

@SpringBootTest
@AutoConfigureMockMvc
class MovementControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private IMovementService movementService;
	
	private Account account;
	
	@BeforeEach
    public void setupTestData(){
        // Given : Setup object or precondition
		account = Account.builder().accountNumber("9876543211").initialBalance(BigDecimal.TEN).status(true)
				.typeAccount(TypeAccount.AHORRO).clientId(2L).build();
	
    }
	
	@Test
	public void getAllMovementsByAccount() throws Exception {
//		Mockito.when(movementService.getAllMovementsByAccount(2L)).thenReturn(Arrays.asList(account));
		mockMvc.perform(MockMvcRequestBuilders.get("/movement/get/2").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.value").value(0));
	}


}
