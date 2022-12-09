package com.sid;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.hibernate.internal.build.AllowPrintStacktrace;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.sid.dtos.CustomerDTO;
import com.sid.entities.AccountOperation;
import com.sid.entities.BankAccount;
import com.sid.entities.CurrentAccount;
import com.sid.entities.Customer;
import com.sid.entities.SavingAccount;
import com.sid.enums.AccountStatus;
import com.sid.enums.OperationType;
import com.sid.exceptions.BalanceNotSufficientException;
import com.sid.exceptions.BankAccountNotFoundExeption;
import com.sid.exceptions.CustomerNotFoundException;
import com.sid.repositories.AccountOperationRepository;
import com.sid.repositories.BankAccountRepository;
import com.sid.repositories.CustomerRepository;
import com.sid.services.BankAccountService;

@SpringBootApplication
public class EbankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingBackendApplication.class, args);
	}

	//@Bean
	CommandLineRunner commandeLineRunner(BankAccountService bankAccountService) {
		return args -> {
			Stream.of("Ali", "Nesrine").forEach(name -> {
				CustomerDTO customer = new CustomerDTO();
				customer.setName(name);
				customer.setEmail(name + "gmail.com");
				bankAccountService.saveCustomer(customer);
			});

			bankAccountService.listCustomer().forEach(cust -> {

				try {
					bankAccountService.saveCurrentBankAccount(Math.random() * 9000, 8000, cust.getId());
					bankAccountService.saveSavingBankAccount(Math.random() * 12000, 5.5, cust.getId());
					List<BankAccount> bankAccounts = bankAccountService.bankAccountList();

					for (BankAccount bankAccount : bankAccounts) {
						
						for (int i = 0; i < 10; i++) {

							bankAccountService.credit(bankAccount.getId(),
									Math.random() * 10000 + Math.random() * 12000, "Credit");
							bankAccountService.debit(bankAccount.getId(), 1000 + Math.random() * 9000, "Debit");
						}
					}
				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				} catch (BankAccountNotFoundExeption | BalanceNotSufficientException e) {
					e.printStackTrace();
				}

			});
		};
	}

	// @Bean
	CommandLineRunner commandeLineRunner(BankAccountRepository bankAccountRepository) {
		return args -> {
			BankAccount bankAccount = bankAccountRepository.findById("7448d2eb-76ed-40a0-96d6-8e124dd5e81b")
					.orElse(null);
			System.out.println(bankAccount.getId());
			System.out.println(bankAccount.getBalance());
			System.out.println(bankAccount.getStatus());
			System.out.println(bankAccount.getCreateAt());
			System.out.println(bankAccount.getCustomer().getName());
			System.out.println(bankAccount.getClass().getSimpleName());
			if (bankAccount instanceof CurrentAccount) {
				System.out.println(((CurrentAccount) bankAccount).getOverDraft());
			} else if (bankAccount instanceof SavingAccount) {
				System.out.println(((SavingAccount) bankAccount).getInterestRate());
			}

			bankAccount.getAccountOperations().forEach(op -> {
				System.out.println("**********************");
				System.out.println(op.getType() + "\t" + op.getOperationDate() + "\t" + op.getAmount());

			});
		};
	}

	// @Bean
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
