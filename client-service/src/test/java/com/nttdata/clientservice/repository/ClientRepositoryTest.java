package com.nttdata.clientservice.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.nttdata.clientservice.model.entities.Client;
import com.nttdata.clientservice.model.enums.GenderType;


@DataJpaTest
class ClientRepositoryTest {
	
	@Autowired
	ClientRepository clientRepository;
	@Autowired
	TestEntityManager entityManager; 
	
	@BeforeEach
	void setUp() {
		Client client = new Client();
	    client.setAddress("conocoto");
	    client.setAge(23);
	    client.setGender(GenderType.MASCULINO);
	    client.setName("Galo");
	    client.setPhone("09927298");
	    client.setIdentification("1709689002");
	    client.setStatus(true);
	    
	    entityManager.persist(client);
		
	}

	@Test
	void test() {
		fail("Not yet implemented");
	}
	
	@Test
	public void whenFindByName_thenReturnClient() {
	    // given
	    Client client = new Client();
	    client.setAddress("conocoto");
	    client.setAge(27);
	    client.setGender(GenderType.MASCULINO);
	    client.setName("Jhoel");
	    client.setPhone("0999273956");
	    client.setStatus(true);
	    client.setIdentification("1719097733");
	    entityManager.persist(client);

	    // when
	    Client found = clientRepository.findByName(client.getName()).get();

	    // then
	    assertThat(found.getName()).isEqualTo(client.getName());
	}

}
