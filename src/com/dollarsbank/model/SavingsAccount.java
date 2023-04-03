// Implemented in Database

package com.dollarsbank.model;

import java.io.Serializable;
import java.util.PriorityQueue;
import java.util.Queue;

public class SavingsAccount implements Serializable {
	// Serializable
	private static final long serialVersionUID = 1L; 
	
	// Variables
	private float balance;
	private Queue<String> transactions;
	private Customer customer;
	private int account = 0;
	
	// Getter and Setter
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
	public Queue<String> getTransactions() {
		return transactions;
	}
	public void setTransactions(Queue<String> transactions) {
		this.transactions = transactions;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public int getAccount() {
		return account;
	}
	
	// Contructors
	public SavingsAccount(float balance, Queue<String> transactions, Customer customer) {
		super();
		this.balance = balance;
		this.transactions = transactions;
		this.customer = customer;
		this.account = account + 1;
	}
	
	public SavingsAccount(Customer customer) {
		super();
		this.balance = 0;
		this.transactions = new PriorityQueue<String>();
		this.customer = customer;
		this.account = account + 1;
	}
}
