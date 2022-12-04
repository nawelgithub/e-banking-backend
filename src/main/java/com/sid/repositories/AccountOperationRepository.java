package com.sid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sid.entities.AccountOperation;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {

}
