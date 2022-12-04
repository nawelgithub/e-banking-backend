package com.sid;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.sid.entities.AccountOperation;
import com.sid.entities.CurrentAccount;
import com.sid.entities.Customer;
import com.sid.entities.SavingAccount;
import com.sid.enums.AccountStatus;
import com.sid.enums.OperationType;
import com.sid.repositories.AccountOperationRepository;
import com.sid.repositories.BankAccountRepository;
import com.sid.repositories.CustomerRepository;

@SpringBootApplication
public class EbankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner start(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository,
			AccountOperationRepository accountOperationRepository) {
		return args -> {
			Stream.of("Hassan", "Yassine", "Aicha").forEach(name -> {
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name + "gmail.com");
				customerRepository.save(customer);
			});
			customerRepository.findAll().forEach(cust -> {
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random() * 90000);
				currentAccount.setCreateAt(new Date());
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setCustomer(cust);
				currentAccount.setOverDraft(9000);
				bankAccountRepository.save(currentAccount);

				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random() * 90000);
				savingAccount.setCreateAt(new Date());
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setCustomer(cust);
				savingAccount.setInterestRate(5.5);
				bankAccountRepository.save(savingAccount);

			});

			bankAccountRepository.findAll().forEach(acc -> {
				for (int i = 0; i < 10; i++) {
					AccountOperation accountOperation = new AccountOperation();

					accountOperation.setOperationDate(new Date());
					accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
					accountOperation.setAmount(Math.random() * 12000);
					accountOperation.setBankAccount(acc);
					accountOperationRepository.save(accountOperation);
				}
			});
		};
	}

}
