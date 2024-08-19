package com.nttdata.movementsservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.nttdata.movementsservice.enums.TypeAccount;
import com.nttdata.movementsservice.enums.TypeMovement;
import com.nttdata.movementsservice.model.entities.Account;
import com.nttdata.movementsservice.model.entities.Movement;

@DataJpaTest
class MovementRepositoryTest {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private MovementRepository movementRepository;
	
	private Account account;
	private Movement movement;
	
	@BeforeEach
    public void setupTestData(){
        // Given : Setup object or precondition
		account = Account.builder().accountNumber("9876543211").initialBalance(BigDecimal.TEN).status(true)
				.typeAccount(TypeAccount.AHORRO).clientId(2L).build();
		movement = Movement.builder().account(account).balance(BigDecimal.TEN).dateMovement(new Date())
				.typeMovement(TypeMovement.CREDITO).value(new BigDecimal(100)).build();		
    }
	
	@Test
    @DisplayName("JUnit test for save movement operation")
    public void givenEmployeeObject_whenSave_thenReturnSaveEmployee(){

        // When : Action of behavious that we are going to test
		Account saveAccount = accountRepository.save(account);
		Movement saveMovement =	movementRepository.save(movement);

        // Then : Verify the output

        assertThat(saveAccount).isNotNull();
        assertThat(saveAccount.getId()).isGreaterThan(0);
        assertThat(saveMovement.getValue()).isEqualTo(new BigDecimal(100));
    }

}
