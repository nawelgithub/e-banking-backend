package com.sid.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.sid.enums.OperationType;

@Entity
public class AccountOperation {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Date operationDate;
	private double amount;
	
	@Enumerated(EnumType.STRING)
	private OperationType type;
	
	@ManyToOne
	private BankAccount bankAccount;

	public AccountOperation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AccountOperation(Long id, Date operationDate, double amount, OperationType type, BankAccount bankAccount) {
		super();
		this.id = id;
		this.operationDate = operationDate;
		this.amount = amount;
		this.type = type;
		this.bankAccount = bankAccount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public OperationType getType() {
		return type;
	}

	public void setType(OperationType type) {
		this.type = type;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}
	
	

}
