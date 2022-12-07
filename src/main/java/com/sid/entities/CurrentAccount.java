package com.sid.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.sid.enums.AccountStatus;

@Entity
@DiscriminatorValue("CA")
public class CurrentAccount extends BankAccount {

	private double overDraft;

	public CurrentAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public CurrentAccount(String id, double balance, Date createAt, AccountStatus status,
			Customer customer, List<AccountOperation> accountOperations) {
		super(id, balance, createAt, status, customer, accountOperations);
		// TODO Auto-generated constructor stub
	}



	public CurrentAccount(double overDraft) {
		super();
		this.overDraft = overDraft;
	}

	public double getOverDraft() {
		return overDraft;
	}

	public void setOverDraft(double overDraft) {
		this.overDraft = overDraft;
	}
	
	
}
