package com.nttdata.clientservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.clientservice.model.entities.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
