package com.sid.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.sid.enums.AccountStatus;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name="TYPE",length = 4, discriminatorType = DiscriminatorType.STRING)
public abstract class BankAccount {
//"TABLE_PER_CLASS" abstract:création seulement des 2 table des classe concrète CurrentAccount et SavingAccount
//"JOINED"	mm avec abstract -> génération des 3 tables 
	@Id 
	private String id;
	private double balance;
	private Date createAt;
	private AccountStatus status;
	
	@ManyToOne
	private Customer customer;
	
	@OneToMany(mappedBy = "bankAccount")
	private List<AccountOperation> accountOperations;
	
	public BankAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BankAccount(String id, double balance, Date createAt, AccountStatus status, Customer customer,
			List<AccountOperation> accountOperations) {
		super();
		this.id = id;
		this.balance = balance;
		this.createAt = createAt;
		this.status = status;
		this.customer = customer;
		this.accountOperations = accountOperations;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<AccountOperation> getAccountOperations() {
		return accountOperations;
	}

	public void setAccountOperations(List<AccountOperation> accountOperations) {
		this.accountOperations = accountOperations;
	}
	
	
	
	
	
}
