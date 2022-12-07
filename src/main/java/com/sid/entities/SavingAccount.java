package com.sid.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.sid.enums.AccountStatus;

@Entity
@DiscriminatorValue("SA")
public class SavingAccount extends BankAccount {

	private double interestRate;

	public SavingAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public SavingAccount(String id, double balance, Date createAt, AccountStatus status,
			Customer customer, List<AccountOperation> accountOperations) {
		super(id, balance, createAt, status, customer, accountOperations);
		// TODO Auto-generated constructor stub
	}



	public SavingAccount(double interestRate) {
		super();
		this.interestRate = interestRate;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
	
	
	
}
