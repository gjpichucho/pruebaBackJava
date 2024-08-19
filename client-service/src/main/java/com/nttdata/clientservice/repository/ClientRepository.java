package com.nttdata.clientservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nttdata.clientservice.model.entities.Client;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByName(String name);
    
    @Query("select c from Client c where c.identification = ?1")
    Optional<Client> findByCi(String name);

}
