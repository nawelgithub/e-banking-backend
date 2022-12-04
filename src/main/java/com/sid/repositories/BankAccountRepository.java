package com.sid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sid.entities.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

}
