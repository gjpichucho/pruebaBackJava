package com.nttdata.movementsservice.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.nttdata.movementsservice.model.entities.Account;
import com.nttdata.movementsservice.model.entities.Movement;



@Repository
public interface MovementRepository extends JpaRepository<Movement, Long>, JpaSpecificationExecutor<Movement> {

    List<Movement> findByAccountOrderByIdDesc(Account account);
    
    List<Movement> findByAccountAndDateMovementBetweenOrderByIdDesc(Account account, Date from, Date to);


}
