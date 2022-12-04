package com.sid.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Customer {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String email;
	
	@OneToMany(mappedBy = "customer")
	private List<BankAccount> bankAccount;
	
	
	
	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Customer(Long id, String name, String email, List<BankAccount> bankAccount) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.bankAccount = bankAccount;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public List<BankAccount> getBankAccount() {
		return bankAccount;
	}



	public void setBankAccount(List<BankAccount> bankAccount) {
		this.bankAccount = bankAccount;
	}



	
	
}
