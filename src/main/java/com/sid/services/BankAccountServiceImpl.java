package com.sid.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sid.entities.AccountOperation;
import com.sid.entities.BankAccount;
import com.sid.entities.CurrentAccount;
import com.sid.entities.Customer;
import com.sid.entities.SavingAccount;
import com.sid.enums.OperationType;
import com.sid.exceptions.BalanceNotSufficientException;
import com.sid.exceptions.BankAccountNotFoundExeption;
import com.sid.exceptions.CustomerNotFoundException;
import com.sid.repositories.AccountOperationRepository;
import com.sid.repositories.BankAccountRepository;
import com.sid.repositories.CustomerRepository;

@Service
@Transactional
public class BankAccountServiceImpl implements BankAccountService{

	private BankAccountRepository bankAccountRepository;
	private CustomerRepository customerRepository;
	private AccountOperationRepository accountOperationRepository;
	
	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, CustomerRepository customerRepository,
			AccountOperationRepository accountOperationRepository) {
		this.bankAccountRepository = bankAccountRepository;
		this.customerRepository = customerRepository;
		this.accountOperationRepository = accountOperationRepository;
	}

	
	@Override
	public Customer saveCustomer(Customer customer) {
		log.info("Saving new Customer");
		Customer savedCustomer = customerRepository.save(customer);
		return savedCustomer;
	}

	@Override
	public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId)throws CustomerNotFoundException
	{
		Customer customer = customerRepository.findById(customerId).orElse(null);
	if (customer == null)
		throw new CustomerNotFoundException("Customer not found");
		
	CurrentAccount currentAccount= new CurrentAccount();
	
	currentAccount.setId(UUID.randomUUID().toString());
	currentAccount.setCreateAt(new Date());
	currentAccount.setBalance(initialBalance);
	currentAccount.setOverDraft(overDraft);
	currentAccount.setCustomer(customer);
	CurrentAccount savedBankAccount =  bankAccountRepository.save(currentAccount);
		return savedBankAccount;
	}


	@Override
	public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId)throws CustomerNotFoundException
	{
		Customer customer = customerRepository.findById(customerId).orElse(null);
	if (customer == null)
		throw new CustomerNotFoundException("Customer not found");
		
	SavingAccount savingAccount = new SavingAccount();
	
	savingAccount.setId(UUID.randomUUID().toString());
	savingAccount.setCreateAt(new Date());
	savingAccount.setBalance(initialBalance);
	savingAccount.setInterestRate(interestRate);
	savingAccount.setCustomer(customer);
	SavingAccount savedBankAccount =  bankAccountRepository.save(savingAccount);
		return savedBankAccount;
	}


	@Override
	public List<Customer> listCustomer() {
		
		return customerRepository.findAll();
	}

	@Override
	public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundExeption {
		BankAccount bankAccount = bankAccountRepository.findById(accountId)
				.orElseThrow(()-> new BankAccountNotFoundExeption("BankAccount not found"));
		return bankAccount;
	}

	@Override
	public void debit(String accountId, double amount, String description) throws BankAccountNotFoundExeption, BalanceNotSufficientException {
	
		BankAccount bankAccount = getBankAccount(accountId);
		if(bankAccount.getBalance() < amount)
			throw new BalanceNotSufficientException("Balance not sufficient");
		
		AccountOperation accountOperation = new AccountOperation();
		accountOperation.setType(OperationType.DEBIT);
		accountOperation.setAmount(amount);
		accountOperation.setDescription(description);
		accountOperation.setOperationDate(new Date());
		accountOperationRepository.save(accountOperation);
		
		bankAccount.setBalance(bankAccount.getBalance()-amount);
		bankAccountRepository.save(bankAccount);
	}

	@Override
	public void credit(String accountId, double amount, String description) throws BankAccountNotFoundExeption{
		BankAccount bankAccount = getBankAccount(accountId);

		AccountOperation accountOperation = new AccountOperation();
		accountOperation.setType(OperationType.DEBIT);
		accountOperation.setAmount(amount);
		accountOperation.setDescription(description);
		accountOperation.setOperationDate(new Date());
		accountOperationRepository.save(accountOperation);
		
		bankAccount.setBalance(bankAccount.getBalance()+amount);
		bankAccountRepository.save(bankAccount);
		
	}

	@Override
	public void transfer(String accountIdSource, String accountIdDestiation, double amount) throws BankAccountNotFoundExeption, BalanceNotSufficientException{
	
		debit(accountIdSource,amount,"Transfer to"+accountIdDestiation);
		credit(accountIdDestiation, amount, "Transfer from"+accountIdSource);	
	}
	
	@Override
	public List<BankAccount> bankAccountList(){
		return bankAccountRepository.findAll();
	}

}
