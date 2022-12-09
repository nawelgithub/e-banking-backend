package com.sid.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sid.dtos.CustomerDTO;
import com.sid.entities.AccountOperation;
import com.sid.entities.BankAccount;
import com.sid.entities.CurrentAccount;
import com.sid.entities.Customer;
import com.sid.entities.SavingAccount;
import com.sid.enums.OperationType;
import com.sid.exceptions.BalanceNotSufficientException;
import com.sid.exceptions.BankAccountNotFoundExeption;
import com.sid.exceptions.CustomerNotFoundException;
import com.sid.mappers.BankAccountMapperImpl;
import com.sid.repositories.AccountOperationRepository;
import com.sid.repositories.BankAccountRepository;
import com.sid.repositories.CustomerRepository;

@Service
@Transactional
public class BankAccountServiceImpl implements BankAccountService {

	private BankAccountRepository bankAccountRepository;
	private CustomerRepository customerRepository;
	private AccountOperationRepository accountOperationRepository;

	private BankAccountMapperImpl dtoMapper;

	Logger log = LoggerFactory.getLogger(this.getClass().getName());

	public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, CustomerRepository customerRepository,
			AccountOperationRepository accountOperationRepository, BankAccountMapperImpl dtoMapper) {
		super();
		this.bankAccountRepository = bankAccountRepository;
		this.customerRepository = customerRepository;
		this.accountOperationRepository = accountOperationRepository;
		this.dtoMapper = dtoMapper;
	}

	@Override
	public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
		log.info("Saving new Customer");
		Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
		Customer savedCustomer = customerRepository.save(customer);
		return dtoMapper.fromCustomer(savedCustomer);
	}

	@Override
	public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId)
			throws CustomerNotFoundException {
		Customer customer = customerRepository.findById(customerId).orElse(null);
		if (customer == null)
			throw new CustomerNotFoundException("Customer not found");

		CurrentAccount currentAccount = new CurrentAccount();

		currentAccount.setId(UUID.randomUUID().toString());
		currentAccount.setCreateAt(new Date());
		currentAccount.setBalance(initialBalance);
		currentAccount.setOverDraft(overDraft);
		currentAccount.setCustomer(customer);
		CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
		return savedBankAccount;
	}

	@Override
	public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId)
			throws CustomerNotFoundException {
		Customer customer = customerRepository.findById(customerId).orElse(null);
		if (customer == null)
			throw new CustomerNotFoundException("Customer not found");

		SavingAccount savingAccount = new SavingAccount();

		savingAccount.setId(UUID.randomUUID().toString());
		savingAccount.setCreateAt(new Date());
		savingAccount.setBalance(initialBalance);
		savingAccount.setInterestRate(interestRate);
		savingAccount.setCustomer(customer);
		SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
		return savedBankAccount;
	}

	@Override
	public List<CustomerDTO> listCustomer() {

		List<Customer> customers = customerRepository.findAll();
		List<CustomerDTO> customersDTOS = customers.stream().map(cust -> dtoMapper.fromCustomer(cust))
				.collect(Collectors.toList());

		/*
		List<CustomerDTO> customersDTOS = new ArrayList<>();
		for (Customer customer : customers) {
			CustomerDTO customerDTO = dtoMapper.fromCustomer(customer);
			customerDTOS.add(customerDTO);
		}
        */
		return customersDTOS;
	}

	@Override
	public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundExeption {
		BankAccount bankAccount = bankAccountRepository.findById(accountId)
				.orElseThrow(() -> new BankAccountNotFoundExeption("BankAccount not found"));
		return bankAccount;
	}

	@Override
	public void debit(String accountId, double amount, String description)
			throws BankAccountNotFoundExeption, BalanceNotSufficientException {

		BankAccount bankAccount = getBankAccount(accountId);
		if (bankAccount.getBalance() < amount)
			throw new BalanceNotSufficientException("Balance not sufficient");

		AccountOperation accountOperation = new AccountOperation();
		accountOperation.setType(OperationType.DEBIT);
		accountOperation.setAmount(amount);
		accountOperation.setDescription(description);
		accountOperation.setOperationDate(new Date());
		accountOperationRepository.save(accountOperation);

		bankAccount.setBalance(bankAccount.getBalance() - amount);
		bankAccountRepository.save(bankAccount);
	}

	@Override
	public void credit(String accountId, double amount, String description) throws BankAccountNotFoundExeption {
		BankAccount bankAccount = getBankAccount(accountId);

		AccountOperation accountOperation = new AccountOperation();
		accountOperation.setType(OperationType.DEBIT);
		accountOperation.setAmount(amount);
		accountOperation.setDescription(description);
		accountOperation.setOperationDate(new Date());
		accountOperationRepository.save(accountOperation);

		bankAccount.setBalance(bankAccount.getBalance() + amount);
		bankAccountRepository.save(bankAccount);

	}

	@Override
	public void transfer(String accountIdSource, String accountIdDestiation, double amount)
			throws BankAccountNotFoundExeption, BalanceNotSufficientException {

		debit(accountIdSource, amount, "Transfer to" + accountIdDestiation);
		credit(accountIdDestiation, amount, "Transfer from" + accountIdSource);
	}

	@Override
	public List<BankAccount> bankAccountList() {
		return bankAccountRepository.findAll();
	}
	
	@Override
	public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
	Customer customer = customerRepository.findById(customerId)
		.orElseThrow(()-> new CustomerNotFoundException("Customer Not found"));
	return dtoMapper.fromCustomer(customer);
	}

	
	@Override
	public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
		log.info("Saving new Customer");
		Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
		Customer savedCustomer = customerRepository.save(customer);
		return dtoMapper.fromCustomer(savedCustomer);
	}
	
	@Override
	public void deleteCustomer(Long customerId) {
		customerRepository.deleteById(customerId);
	}
}
