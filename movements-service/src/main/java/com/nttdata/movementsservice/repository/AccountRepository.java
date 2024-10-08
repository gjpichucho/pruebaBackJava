package com.nttdata.movementsservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.movementsservice.model.entities.Account;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByClientId(Long clientId);

    Optional<Account> findByAccountNumber(String accountNumber);

}
