package com.sid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sid.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
