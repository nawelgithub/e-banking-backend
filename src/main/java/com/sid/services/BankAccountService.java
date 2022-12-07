package com.sid.services;

import java.util.List;

import com.sid.entities.BankAccount;
import com.sid.entities.CurrentAccount;
import com.sid.entities.Customer;
import com.sid.entities.SavingAccount;
import com.sid.exceptions.BalanceNotSufficientException;
import com.sid.exceptions.BankAccountNotFoundExeption;
import com.sid.exceptions.CustomerNotFoundException;

public interface BankAccountService {

	Customer saveCustomer(Customer customer);

	CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft ,Long customerId) throws CustomerNotFoundException;
	
	SavingAccount saveSavingBankAccount(double initialBalance, double interestRate ,Long customerId) throws CustomerNotFoundException;
	
	List<Customer> listCustomer();

	BankAccount getBankAccount(String accountId) throws BankAccountNotFoundExeption;

	void debit(String accountId, double amount, String description) throws BankAccountNotFoundExeption, BalanceNotSufficientException;

	void credit(String accountId, double amount, String description) throws BankAccountNotFoundExeption;

	void transfer(String accountIdSource, String accountIdDestiation, double amount) throws BankAccountNotFoundExeption, BalanceNotSufficientException;
	
	List<BankAccount> bankAccountList();
}
